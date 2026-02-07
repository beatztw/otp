package ru.chugunov.otp.config;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.chugunov.otp.dto.responses.SendOtpKafkaResponse;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;

@Configuration
public class CaffeineCacheConfig {

    @Value("${otp.cache.kafka-send-otp.expire-after-write-minutes:1}")
    private Long expireAfterWriteMinutes;

    @Value("${otp.cache.kafka-send-otp.maximum-size:500}")
    private Long maximumCacheSize;

    @Bean
    public Cache<String, CompletableFuture<SendOtpKafkaResponse>> kafkaResponseCache() {
        return Caffeine.newBuilder()
                .expireAfterWrite(Duration.ofMinutes(expireAfterWriteMinutes))
                .maximumSize(maximumCacheSize)
                .build();
    }
}
