package ru.chugunov.otp.exception;

public class KafkaSendOtpException extends RuntimeException {

    public KafkaSendOtpException(String message) {
        super(message);
    }
}
