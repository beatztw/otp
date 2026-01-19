package ru.chugunov.otp.exception;

import org.springframework.http.HttpStatus;

public class KafkaSendOtpException extends ServiceException {

    public KafkaSendOtpException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
