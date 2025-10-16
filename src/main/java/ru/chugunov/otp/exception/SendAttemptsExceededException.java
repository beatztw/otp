package ru.chugunov.otp.exception;

public class SendAttemptsExceededException extends OtpException {

    public SendAttemptsExceededException() {
        super("Превышено количество отправок OTP");
    }
}
