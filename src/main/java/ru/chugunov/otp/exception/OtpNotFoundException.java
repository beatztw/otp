package ru.chugunov.otp.exception;

public class OtpNotFoundException extends BusinessException {

    public OtpNotFoundException() {
        super("Не удалось найти информацию об отправленном OTP");
    }
}
