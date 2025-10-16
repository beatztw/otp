package ru.chugunov.otp.exception;

public class OtpExpiredException extends OtpException {

    public OtpExpiredException() {
        super("Время жизни OTP истекло");
    }
}
