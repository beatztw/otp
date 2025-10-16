package ru.chugunov.otp.service.impl;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.chugunov.otp.controllers.enums.SendOtpStatus;
import ru.chugunov.otp.dto.requests.CheckOtpRequest;
import ru.chugunov.otp.dto.requests.GeneratedOtpRequest;
import ru.chugunov.otp.exception.*;
import ru.chugunov.otp.mapper.CheckOtpMapper;
import ru.chugunov.otp.mapper.SendOtpMapper;
import ru.chugunov.otp.model.CheckOtp;
import ru.chugunov.otp.model.SendOtp;
import ru.chugunov.otp.repository.CheckOtpRepository;
import ru.chugunov.otp.repository.SendOtpRepository;
import ru.chugunov.otp.service.OtpService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OtpServiceImpl implements OtpService {

    private final CheckOtpRepository checkOtpRepository;
    private final SendOtpRepository sendOtpRepository;
    private final PasswordEncoder passwordEncoder;
    private final CheckOtpMapper checkOtpMapper;
    private final SendOtpMapper sendOtpMapper;

    @Override
    public void generateAndSendOtp(GeneratedOtpRequest request) {
        validateSendOtpRequest(request);

        String generatedOtp = RandomStringUtils.randomNumeric(request.getLength());
        String encodedOtp = passwordEncoder.encode(request.getProcessID() + generatedOtp);
        String message = String.format(request.getMessage(), encodedOtp);

        SendOtp sendOtpForSave = buildSendOtp(request, encodedOtp);
        sendOtpRepository.save(sendOtpForSave);

        sendOtp(request, generatedOtp, message);
    }

    @Override
    public void checkOtp(CheckOtpRequest request) {
        SendOtp latestOtp = sendOtpRepository.findFirstByProcessIdOrderByCreateTimeDesc(
                String.valueOf(request.getProcessID())
            ).orElseThrow(OtpNotFoundException::new);

        validateLatestOtp(latestOtp);
        checkIfAlreadyVerified(request);
        verifyOtp(request, latestOtp);

        saveOtp(request, true);
    }

    private void sendOtp(GeneratedOtpRequest request, String generatedOtp, String message) {
        switch (request.getSendingChannel()) {
            // TODO: Реализовать логику отправки через кафка
            case TELEGRAM -> System.out.println("Одноразовый пароль для телеграмма: " + generatedOtp);

            case CONSOLE -> System.out.println("Одноразовый пароль: " + generatedOtp);
            default ->
                    throw new OtpException(String.format("Неизвестный канал отправки %s", request.getSendingChannel()));
        }
    }

    private void validateSendOtpRequest(GeneratedOtpRequest request) {
        List<SendOtp> sendOtpList = sendOtpRepository.findAllByProcessIdOrderByCreateTimeAsc(
                String.valueOf(request.getProcessID())
        );

        if (!sendOtpList.isEmpty()) {
            LocalDateTime now = LocalDateTime.now();
            SendOtp latestOtp = sendOtpList.get(sendOtpList.size() - 1);
            LocalDateTime createTime = latestOtp.getCreateTime();

            if (createTime.plusSeconds(request.getSessionTtl()).isBefore(now)) {
                throw new SessionTtlOtpExceededException();
            }

            if (createTime.plusSeconds(request.getResendTimeout()).isBefore(now)) {
                throw new ResendOtpFrequencyExceededException();
            }

            if (sendOtpList.size() >= sendOtpList.get(0).getResendAttempts()) {
                throw new SendAttemptsExceededException();
            }
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

    private void validateLatestOtp(SendOtp latestOtp) {
        if (latestOtp.getCreateTime().plusSeconds(latestOtp.getTtl()).isBefore(LocalDateTime.now())) {
            throw new OtpExpiredException();
        }
    }
    private void checkIfAlreadyVerified(CheckOtpRequest request) {
        boolean alreadyVerified = checkOtpRepository.existsByProcessIdAndOtpAndCorrectTrue(
                String.valueOf(request.getProcessID()),
                request.getOtp()
        );

        if (alreadyVerified) {
            saveOtp(request, false);

            throw new OtpAlreadyVerifiedException();
        }
    }

    private void verifyOtp(CheckOtpRequest request, SendOtp latestOtp) {
        boolean isCorrectOtp = passwordEncoder.matches(
                request.getProcessID() + request.getOtp(),
                latestOtp.getEncodedOtp()
        );

        if (!isCorrectOtp) {
            saveOtp(request, false);

            throw new InvalidOtpException();
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
