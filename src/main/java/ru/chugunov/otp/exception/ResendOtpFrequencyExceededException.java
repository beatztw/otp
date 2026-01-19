package ru.chugunov.otp.exception;

public class ResendOtpFrequencyExceededException extends BusinessException {

    public ResendOtpFrequencyExceededException(String message) {
        super(message);
    }
}
