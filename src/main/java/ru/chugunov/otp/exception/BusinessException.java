package ru.chugunov.otp.exception;

public class BusinessException extends RuntimeException {

    public BusinessException(String message){
        super(message);
    }

}
