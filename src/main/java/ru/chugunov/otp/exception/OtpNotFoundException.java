package ru.chugunov.otp.exception;

public class OtpNotFoundException extends OtpException {

    public OtpNotFoundException() {
        super("Не удалось найти информацию об отправленном OTP");
    }
}
