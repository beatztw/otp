package ru.chugunov.otp.exception;

public class SessionTtlOtpExceededException extends OtpException {

    public SessionTtlOtpExceededException() {
        super("Превышено время жизни сессии для отправки OTP");
    }
}
