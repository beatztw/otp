package ru.chugunov.otp.exception;

public class SessionTtlOtpExceededException extends BusinessException {

    public SessionTtlOtpExceededException(String message) {
        super(message);
    }
}
