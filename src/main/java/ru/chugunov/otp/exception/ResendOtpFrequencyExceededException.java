package ru.chugunov.otp.exception;

public class ResendOtpFrequencyExceededException extends BusinessException {

    public ResendOtpFrequencyExceededException() {
        super("Превышена частота попыток отправки OTP");
    }
}
