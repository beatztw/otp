package ru.chugunov.otp.exception;

public class SendAttemptsExceededException extends OtpException {

    public SendAttemptsExceededException(String message) {
        super(message);
    }
}
