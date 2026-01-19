package ru.chugunov.otp.service;

import ru.chugunov.otp.dto.responses.SendOtpKafkaResponse;

public interface KafkaOtpService {

    SendOtpKafkaResponse sendOtpToTelegram(String telegramChatId, String message, String sendMessageKey);
}
