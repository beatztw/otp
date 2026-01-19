package ru.chugunov.otp.kafka;

import com.github.benmanes.caffeine.cache.Cache;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import ru.chugunov.otp.dto.responses.SendOtpKafkaResponse;

import java.util.concurrent.CompletableFuture;

@Slf4j
@Component
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "otp.kafka.send-otp", name = "enabled", havingValue = "true")
public class KafkaMessageContext {

    private final Cache<String, CompletableFuture<SendOtpKafkaResponse>> messageContext;

    public CompletableFuture<SendOtpKafkaResponse> createMessageCompletableFuture(String id) {
        CompletableFuture<SendOtpKafkaResponse> completableFuture = new CompletableFuture<>();
        messageContext.put(id, completableFuture);

        log.info("Создан CompletableFuture для сообщения c id = {}", id);
        return completableFuture;
    }

    public CompletableFuture<SendOtpKafkaResponse> findById(String id) {
        CompletableFuture<SendOtpKafkaResponse> future = messageContext.getIfPresent(id);

        if (future == null) {
            log.debug("CompletableFuture c id = {} не найден или истек таймаут", id);
        }

        return future;
    }

    public void removeById(String id) {
        messageContext.invalidate(id);
    }
}
