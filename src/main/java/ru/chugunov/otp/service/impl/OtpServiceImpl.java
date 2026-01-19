package ru.chugunov.otp.service.impl;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.chugunov.otp.controllers.enums.SendOtpKafkaStatus;
import ru.chugunov.otp.controllers.enums.SendOtpStatus;
import ru.chugunov.otp.dto.requests.CheckOtpRequest;
import ru.chugunov.otp.dto.requests.GeneratedOtpRequest;
import ru.chugunov.otp.dto.responses.SendOtpKafkaResponse;
import ru.chugunov.otp.exception.*;
import ru.chugunov.otp.mapper.CheckOtpMapper;
import ru.chugunov.otp.mapper.SendOtpMapper;
import ru.chugunov.otp.model.CheckOtp;
import ru.chugunov.otp.model.SendOtp;
import ru.chugunov.otp.repository.CheckOtpRepository;
import ru.chugunov.otp.repository.SendOtpRepository;
import ru.chugunov.otp.service.KafkaOtpService;
import ru.chugunov.otp.service.OtpService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OtpServiceImpl implements OtpService {

    private final KafkaOtpService kafkaOtpService;

    private final CheckOtpRepository checkOtpRepository;

    private final SendOtpRepository sendOtpRepository;

    private final PasswordEncoder passwordEncoder;

    private final CheckOtpMapper checkOtpMapper;

    private final SendOtpMapper sendOtpMapper;

    @Override
    @Transactional
    public void generateAndSendOtp(GeneratedOtpRequest request) {
        Optional<SendOtp> latestSendOtp = sendOtpRepository.findFirstByProcessIdAndOrderByCreateTimeDesc(
                String.valueOf(request.getProcessID())
        );

        if (latestSendOtp.isPresent()) {
            LocalDateTime now = LocalDateTime.now();
            SendOtp latestOtp = latestSendOtp.get();
            LocalDateTime createTime = latestOtp.getCreateTime();

            if (createTime.plusSeconds(request.getSessionTtl()).isBefore(now)) {
                throw new SessionTtlOtpExceededException("Превышено время жизни сессии для отправки OTP");
            }

            if (createTime.plusSeconds(request.getResendTimeout()).isAfter(now)) {
                throw new ResendOtpFrequencyExceededException("Превышена частота попыток отправки OTP");
            }
        }

        List<SendOtp> sendOtpList = sendOtpRepository.findAllByProcessIdOrderByCreateTimeAsc(
                String.valueOf(request.getProcessID())
        );

        if (!sendOtpList.isEmpty()) {
            if (sendOtpList.size() >= sendOtpList.get(0).getResendAttempts()) {
                throw new SendAttemptsExceededException("Превышено количество отправок OTP");
            }
        }

        String generatedOtp = RandomStringUtils.randomNumeric(request.getLength());
        String encodedOtp = passwordEncoder.encode(request.getProcessID() + generatedOtp);
        String message = String.format(request.getMessage(), generatedOtp);

        SendOtp sendOtpForSave = buildSendOtp(request, encodedOtp);
        sendOtpRepository.save(sendOtpForSave);

        String sendMessageKey = sendOtpForSave.getSendMessageKey();

        sendOtp(request, generatedOtp, message, sendMessageKey);
    }

    @Override
    public void checkOtp(CheckOtpRequest request) {
        SendOtp latestOtp = sendOtpRepository.findFirstByProcessIdAndOrderByCreateTimeDesc(
                String.valueOf(request.getProcessID())
        ).orElseThrow(() -> new OtpNotFoundException("Не удалось найти информацию об отправленном OTP"));

        LocalDateTime now = LocalDateTime.now();
        if (latestOtp.getCreateTime().plusSeconds(latestOtp.getTtl()).isBefore(now)) {
            throw new OtpExpiredException("Время жизни OTP истекло");
        }

        checkIfAlreadyVerified(request);

        verifyOtp(request, latestOtp);

//        CheckOtp correctOtp = buildCheckOtp(request, true);
        saveOtp(request, true);
    }

    private void sendOtp(GeneratedOtpRequest request, String generatedOtp, String message, String sendMessageKey) {
        switch (request.getSendingChannel()) {
            case TELEGRAM -> {
                SendOtpKafkaResponse response =
                        kafkaOtpService.sendOtpToTelegram(request.getTarget(), message, sendMessageKey);

                SendOtp otpRecord = sendOtpRepository.findBySendMessageKey(sendMessageKey)
                        .orElseThrow(() -> new OtpNotFoundException(
                                String.format("Не удалось найти информацию об отправленном OTP с message key %s",
                                        sendMessageKey))
                        );

                if (SendOtpKafkaStatus.ERROR.equals(response.getStatus())) {
                    otpRecord.setStatus(SendOtpStatus.ERROR);
                    sendOtpRepository.save(otpRecord);

                    throw new KafkaSendOtpException(response.getErrorMessage());
                } else if (SendOtpKafkaStatus.SUCCESS.equals(response.getStatus())) {
                    otpRecord.setStatus(SendOtpStatus.DELIVERED);
                    sendOtpRepository.save(otpRecord);
                }
            }

            case CONSOLE -> System.out.println("Одноразовый пароль: " + generatedOtp);
            default ->
                    throw new BusinessException(String.format("Неизвестный канал отправки %s", request.getSendingChannel()));
        }
    }

    private SendOtp buildSendOtp(GeneratedOtpRequest request, String encodedOtp) {
        SendOtp sendOtp = sendOtpMapper.fromGeneratedOtpRequestToEntity(request);

        sendOtp.setEncodedOtp(encodedOtp);
        sendOtp.setProcessId(String.valueOf(request.getProcessID()));
        sendOtp.setSendMessageKey(String.valueOf(UUID.randomUUID()));
        sendOtp.setStatus(SendOtpStatus.IN_PROCESS);
        sendOtp.setSendTime(LocalDateTime.now());

        return sendOtp;
    }

    private void checkIfAlreadyVerified(CheckOtpRequest request) {
        boolean alreadyVerified = checkOtpRepository.existsByProcessIdAndOtpAndCorrectTrue(
                String.valueOf(request.getProcessID()),
                request.getOtp()
        );

        if (alreadyVerified) {
//            CheckOtp notAlreadyVerifiedOtp = buildCheckOtp(request, false);
            saveOtp(request, false);

            throw new OtpAlreadyVerifiedException("Попытка подтверждения ранее подтвержденного OTP");
        }
    }

    private void verifyOtp(CheckOtpRequest request, SendOtp latestOtp) {
        boolean isCorrectOtp = passwordEncoder.matches(
                request.getProcessID() + request.getOtp(),
                latestOtp.getEncodedOtp()
        );

        if (!isCorrectOtp) {
//            CheckOtp incorrectOtp = buildCheckOtp(request, false);
            saveOtp(request, false);

            throw new InvalidOtpException("Введен неверный OTP");
        }
    }

    public void saveOtp(CheckOtpRequest request, boolean isCorrect) {
        CheckOtp otp = buildCheckOtp(request, isCorrect);
        checkOtpRepository.save(otp);
    }

    private CheckOtp buildCheckOtp(CheckOtpRequest request, boolean isCorrect) {
        CheckOtp checkOtp = checkOtpMapper.fromCheckOtpRequestToEntity(request);

        checkOtp.setProcessId(String.valueOf(request.getProcessID()));
        checkOtp.setCheckTime(LocalDateTime.now());
        checkOtp.setCorrect(isCorrect);

        return checkOtp;
    }

}
