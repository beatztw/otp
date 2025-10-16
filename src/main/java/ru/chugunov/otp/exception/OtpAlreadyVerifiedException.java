package ru.chugunov.otp.exception;

public class OtpAlreadyVerifiedException extends OtpException {

    public OtpAlreadyVerifiedException() {
        super("Попытка подтверждения ранее подтвержденного OTP");
    }
}
