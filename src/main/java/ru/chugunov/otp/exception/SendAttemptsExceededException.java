package ru.chugunov.otp.exception;

public class SendAttemptsExceededException extends BusinessException {

    public SendAttemptsExceededException() {
        super("Превышено количество отправок OTP");
    }
}
