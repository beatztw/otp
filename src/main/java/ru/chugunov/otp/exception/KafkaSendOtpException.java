package ru.chugunov.otp.exception;

public class KafkaSendOtpException extends BusinessException {

    public KafkaSendOtpException(String message) {
        super(message);
    }
}
