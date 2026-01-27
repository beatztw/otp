package ru.chugunov.otp.service;

import ru.chugunov.otp.controllers.enums.SendOtpStatus;
import ru.chugunov.otp.dto.requests.CheckOtpRequest;
import ru.chugunov.otp.dto.requests.GeneratedOtpRequest;

public interface OtpService {

    void generateAndSendOtp(GeneratedOtpRequest request);

    void checkOtp(CheckOtpRequest request);

    void updateSendOtpStatus(String sendMessageKey, SendOtpStatus status);
}
