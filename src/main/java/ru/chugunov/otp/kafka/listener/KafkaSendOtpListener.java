package ru.chugunov.otp.kafka.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import ru.chugunov.otp.dto.responses.SendOtpKafkaResponse;
import ru.chugunov.otp.kafka.KafkaMessageContext;
import ru.chugunov.otp.utils.JsonUtils;

import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "otp.kafka.send-otp", name = "enabled", havingValue = "true")
public class KafkaSendOtpListener {

    private final JsonUtils jsonUtils;
    private final KafkaMessageContext kafkaMessageContext;

    @KafkaListener(topics = "${otp.kafka.send-otp.out.topic}", groupId = "${spring.kafka.consumer.group-id}")
    public void receiveResponse(ConsumerRecord<String, String> consumerRecord,
                                @Payload(required = false) String payload) {

        if (payload == null || payload.trim().isEmpty()) {
            log.info("Получено пустое сообщение от сервера");
            return;
        }

        try {
            SendOtpKafkaResponse response = jsonUtils.fromJson(consumerRecord.value(), SendOtpKafkaResponse.class);
            log.info("Получен ответ c id = {} и status = {}", response.getId(), response.getStatus());

            CompletableFuture<SendOtpKafkaResponse> responseCompletableFuture = kafkaMessageContext
                    .findById(response.getId());

            if (responseCompletableFuture == null) {
                log.info("Получено сообщение в ответный топик для которого не был найден callback: {}", response);
            } else {
                responseCompletableFuture.complete(response);
            }

        } catch (Exception e) {
            log.error("Ошибка при обработке ответа от Kafka", e);
        }
    }
}
