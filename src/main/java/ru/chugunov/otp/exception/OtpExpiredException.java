package ru.chugunov.otp.exception;

public class OtpExpiredException extends BusinessException {

    public OtpExpiredException(String message) {
        super(message);
    }
}
