package ru.chugunov.otp.exception;

public class InvalidOtpException extends OtpException {

    public InvalidOtpException() {
        super("Введен неверный OTP");
    }
}
