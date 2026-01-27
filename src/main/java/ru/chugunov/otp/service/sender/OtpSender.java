package ru.chugunov.otp.service.sender;

import ru.chugunov.otp.controllers.enums.SendingChannel;

public interface OtpSender {

    SendingChannel getSendingChannel();

    void sendOtp(String target, String message, String sendMessageKey);
}
