package ru.chugunov.otp.config;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.chugunov.otp.dto.responses.SendOtpKafkaResponse;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;

import static ru.chugunov.otp.utils.Constants.TTL_AFTER_WRITE_CACHE;

@Configuration
public class CaffeineCacheConfig {

    @Bean
    public Cache<String, CompletableFuture<SendOtpKafkaResponse>> kafkaResponseCache() {
        return Caffeine.newBuilder()
                .expireAfterWrite(Duration.ofMinutes(TTL_AFTER_WRITE_CACHE))
                .build();
    }
}
