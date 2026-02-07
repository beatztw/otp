package ru.chugunov.otp.kafka.producer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import ru.chugunov.otp.dto.requests.SendOtpKafkaRequest;
import ru.chugunov.otp.dto.responses.SendOtpKafkaResponse;
import ru.chugunov.otp.exception.KafkaSendOtpException;
import ru.chugunov.otp.kafka.KafkaMessageContext;
import ru.chugunov.otp.utils.JsonUtils;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Slf4j
@Component
@RequiredArgsConstructor
public class SendOtpKafkaProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final KafkaMessageContext kafkaMessageContext;
    private final JsonUtils jsonUtils;

    @Value("${otp.kafka.send-otp.topic-in}")
    private String topicIn;

    @Value("${otp.kafka.send-otp.response-timeout:3}")
    private Long kafkaResponseTimeout;

    public SendOtpKafkaResponse sendOtp(SendOtpKafkaRequest request) {
        CompletableFuture<SendOtpKafkaResponse> responseCompletableFuture =
                kafkaMessageContext.createMessageCompletableFuture(request.getId());

        kafkaTemplate.send(topicIn, request.getId(), jsonUtils.toJson(request));

        try {
            return responseCompletableFuture
                    .get(kafkaResponseTimeout, TimeUnit.SECONDS);
        } catch (TimeoutException e) {
            throw new KafkaSendOtpException("Таймаут ожидания ответа от сервиса отправки сообщений");
        } catch (InterruptedException | ExecutionException e) {
            throw new KafkaSendOtpException(String.format("Ошибка при отправке OTP через Kafka. id = %s",
                    request.getId()));
        } finally {
            kafkaMessageContext.removeById(request.getId());
        }
    }
}
