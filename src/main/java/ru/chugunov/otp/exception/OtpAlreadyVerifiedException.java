package ru.chugunov.otp.exception;

public class OtpAlreadyVerifiedException extends OtpException {

    public OtpAlreadyVerifiedException(String message) {
        super(message);
    }
}
