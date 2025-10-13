package ru.chugunov.otp.exception;

public class ResendOtpFrequencyExceededException extends OtpException {

    public ResendOtpFrequencyExceededException(String message) {
        super(message);
    }
}
