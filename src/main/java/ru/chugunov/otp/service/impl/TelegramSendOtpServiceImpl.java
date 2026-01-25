package ru.chugunov.otp.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.chugunov.otp.controllers.enums.SendOtpStatus;
import ru.chugunov.otp.dto.requests.SendOtpKafkaRequest;
import ru.chugunov.otp.dto.responses.SendOtpKafkaResponse;
import ru.chugunov.otp.exception.BusinessException;
import ru.chugunov.otp.exception.KafkaSendOtpException;
import ru.chugunov.otp.exception.OtpNotFoundException;
import ru.chugunov.otp.kafka.KafkaMessageContext;
import ru.chugunov.otp.model.SendOtp;
import ru.chugunov.otp.repository.SendOtpRepository;
import ru.chugunov.otp.service.TelegramSendOtpService;
import ru.chugunov.otp.utils.JsonUtils;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static ru.chugunov.otp.utils.Constants.RESPONSE_KAFKA_COMPLETABLE_FUTURE_TIMEOUT;

@Slf4j
@Service
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "otp.kafka.send-otp", name = "enabled", havingValue = "true")
public class TelegramSendOtpServiceImpl implements TelegramSendOtpService {

    private final KafkaTemplate<String, String> kafkaTemplate;

    private final KafkaMessageContext kafkaMessageContext;

    private final SendOtpRepository sendOtpRepository;

    private final JsonUtils jsonUtils;

    @Value("${otp.kafka.send-otp.topic-in}")
    private String topicIn;

    @Override
    public SendOtpKafkaResponse sendOtpToTelegram(String telegramChatId,
                                                  String message,
                                                  String sendMessageKey) {
        SendOtpKafkaRequest kafkaRequest = SendOtpKafkaRequest.builder()
                .id(sendMessageKey)
                .message(message)
                .telegramChatId(telegramChatId)
                .build();

        CompletableFuture<SendOtpKafkaResponse> responseCompletableFuture =
                kafkaMessageContext.createMessageCompletableFuture(kafkaRequest.getId());

        kafkaTemplate.send(topicIn, kafkaRequest.getId(), jsonUtils.toJson(kafkaRequest));

        try {
            SendOtpKafkaResponse response = responseCompletableFuture
                    .get(RESPONSE_KAFKA_COMPLETABLE_FUTURE_TIMEOUT, TimeUnit.SECONDS);

            log.info("Получен ответ от сервиса отправки для id = {} со статусом {}",
                    sendMessageKey, response.getStatus());

            return response;
        } catch (TimeoutException e) {
            SendOtp otpRecord = sendOtpRepository.findBySendMessageKey(sendMessageKey)
                    .orElseThrow(() -> new OtpNotFoundException(
                            String.format("Не удалось найти информацию об отправленном OTP с message key %s",
                                    sendMessageKey))
                    );

            otpRecord.setStatus(SendOtpStatus.ERROR);
            sendOtpRepository.save(otpRecord);

            throw new KafkaSendOtpException("Таймаут ожидания ответа от сервиса отправки сообщений");
        } catch (InterruptedException | ExecutionException e) {
            throw new BusinessException(String.format("Ошибка при отправке OTP через Kafka. id = %s",
                    kafkaRequest.getId()));
        } finally {
            kafkaMessageContext.removeById(kafkaRequest.getId());
        }
    }
}
