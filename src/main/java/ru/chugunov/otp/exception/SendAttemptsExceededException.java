package ru.chugunov.otp.exception;

public class SendAttemptsExceededException extends BusinessException {

    public SendAttemptsExceededException(String message) {
        super(message);
    }
}
