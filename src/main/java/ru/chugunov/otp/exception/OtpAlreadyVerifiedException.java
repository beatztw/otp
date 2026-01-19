package ru.chugunov.otp.exception;

public class OtpAlreadyVerifiedException extends BusinessException {

    public OtpAlreadyVerifiedException(String message) {
        super(message);
    }
}
