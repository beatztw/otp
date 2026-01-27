package ru.chugunov.otp.exception;

public class JsonConversionException extends BusinessException {
    public JsonConversionException() {
        super("Ошибка преобразования объекта в JSON");
    }
}
