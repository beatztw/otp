package ru.chugunov.otp.exception;

public class SessionTtlOtpExceededException extends OtpException {

    public SessionTtlOtpExceededException(String message) {
        super(message);
    }
}
