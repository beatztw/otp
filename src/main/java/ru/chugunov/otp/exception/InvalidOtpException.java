package ru.chugunov.otp.exception;

public class InvalidOtpException extends BusinessException {

    public InvalidOtpException() {
        super("Введен неверный OTP");
    }
}
