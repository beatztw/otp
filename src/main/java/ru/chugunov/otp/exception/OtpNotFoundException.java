package ru.chugunov.otp.exception;

public class OtpNotFoundException extends BusinessException {

    public OtpNotFoundException(String message) {
        super(message);
    }
}
