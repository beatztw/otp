package ru.chugunov.otp.exception;

public class ResendOtpFrequencyExceededException extends OtpException {

    public ResendOtpFrequencyExceededException() {
        super("Превышена частота попыток отправки OTP");
    }
}
