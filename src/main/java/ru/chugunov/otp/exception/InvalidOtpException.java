package ru.chugunov.otp.exception;

public class InvalidOtpException extends BusinessException {

    public InvalidOtpException(String message) {
        super(message);
    }
}
