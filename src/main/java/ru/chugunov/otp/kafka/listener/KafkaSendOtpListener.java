package ru.chugunov.otp.kafka.listener;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.security.oauthbearer.internals.secured.ValidateException;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import ru.chugunov.otp.dto.responses.SendOtpKafkaResponse;
import ru.chugunov.otp.kafka.KafkaMessageContext;
import ru.chugunov.otp.utils.JsonUtils;

import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "otp.kafka.send-otp", name = "enabled", havingValue = "true")
public class KafkaSendOtpListener {

    private final JsonUtils jsonUtils;
    private final Validator validator;
    private final KafkaMessageContext kafkaMessageContext;

    @KafkaListener(topics = "${otp.kafka.send-otp.topic-out}", groupId = "${spring.kafka.consumer.group-id}")
    public void consumeSendOtp(ConsumerRecord<String, String> consumerRecord) {

        if (!StringUtils.hasText(consumerRecord.value())) {
            log.info("Получено пустое сообщение от сервера");
            return;
        }

        try {
            SendOtpKafkaResponse response = jsonUtils.fromJson(consumerRecord.value(), SendOtpKafkaResponse.class);
            log.info("Получен ответ от сервиса отправки для id = {} со статусом {}",
                    response.getId(), response.getStatus());

            validateMessage(response);

            notifyWaitingCallback(response);

        } catch (Exception e) {
            log.error("Ошибка при обработке ответа от Kafka", e);
        }
    }

    private void notifyWaitingCallback(SendOtpKafkaResponse response) {
        CompletableFuture<SendOtpKafkaResponse> responseCompletableFuture = kafkaMessageContext
                .findById(response.getId());

        if (responseCompletableFuture == null) {
            log.info("Получено сообщение в ответный топик для которого не был найден callback: {}", response);
        } else {
            responseCompletableFuture.complete(response);
        }
    }

    private <T> void validateMessage(T event) {
        Set<ConstraintViolation<T>> validationErrors = validator.validate(event);

        if (CollectionUtils.isEmpty(validationErrors)) {
            return;
        }

        String validationErrorMessage = validationErrors.stream()
                .map(ve -> ve.getMessage() + " \"" + ve.getPropertyPath() + "\":\"" + ve.getInvalidValue() + "\"")
                .collect(Collectors.joining(", "));

        throw new ValidateException("[" + validationErrorMessage + "]");
    }
}
