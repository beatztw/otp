package ru.chugunov.otp.exception;

public class OtpAlreadyVerifiedException extends BusinessException {

    public OtpAlreadyVerifiedException() {
        super("Попытка подтверждения ранее подтвержденного OTP");
    }
}
