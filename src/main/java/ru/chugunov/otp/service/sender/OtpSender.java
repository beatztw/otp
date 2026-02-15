package ru.chugunov.otp.service.sender;

import ru.chugunov.otp.model.SendingChannel;

public interface OtpSender {

    SendingChannel getSendingChannel();

    void sendOtp(String target, String message, String sendMessageKey);
}
