package ru.chugunov.otp.exception;

public class SessionTtlOtpExceededException extends BusinessException {

    public SessionTtlOtpExceededException() {
        super("Превышено время жизни сессии для отправки OTP");
    }
}
