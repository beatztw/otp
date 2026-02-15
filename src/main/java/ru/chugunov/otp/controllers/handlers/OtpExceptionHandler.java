package ru.chugunov.otp.controllers.handlers;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.chugunov.otp.dto.common.CommonResponse;
import ru.chugunov.otp.dto.common.ValidationError;
import ru.chugunov.otp.exception.BusinessException;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class OtpExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public CommonResponse<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        BindingResult bindingResult = e.getBindingResult();

        List<ValidationError> validationErrors = new ArrayList<>();

        bindingResult.getAllErrors().forEach(error -> {
            String errorMessage = error.getDefaultMessage();

            String fieldName = null;

            if (error instanceof FieldError) {
                fieldName = ((FieldError) error).getField();
            }
            ValidationError validationError = ValidationError.builder()
                    .field(fieldName)
                    .message(errorMessage)
                    .build();

            validationErrors.add(validationError);
        });

        log.warn("Ошибка валидации: {}", validationErrors, e);

        return CommonResponse.builder()
                .id(UUID.randomUUID())
                .errorMessage("Ошибка валидации")
                .validationErrors(validationErrors)
                .build();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public CommonResponse<?> handleHttpMessageNotReadableException(HttpMessageNotReadableException e){
        if (e.getCause() instanceof InvalidFormatException ife) {
            String path = ife.getPath().stream()
                    .map(JsonMappingException.Reference::getFieldName)
                    .collect(Collectors.joining("."));

            String errMessage = "Ошибка валидации, указан некорректный формат поля '" + path + "'";

            log.warn(errMessage, e);

            return CommonResponse.builder()
                    .id(UUID.randomUUID())
                    .errorMessage(errMessage)
                    .build();
        }

        return handleException(e);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(Exception.class)
    public CommonResponse<?> handleException(Exception e){
        log.error("Непредвиденное исключение: {}", e.getMessage(), e);

        return CommonResponse.builder()
                .id(UUID.randomUUID())
                .errorMessage("Непредвиденное исключение: " + e.getMessage())
                .build();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BusinessException.class)
    public CommonResponse<?> handleOtpException(BusinessException e){
        log.warn("Перехвачена ошибка выполнения бизнес-логики: {}", e.getMessage(), e);

        return CommonResponse.builder()
                .id(UUID.randomUUID())
                .errorMessage("Ошибка выполнения бизнес-логики: " + e.getMessage())
                .build();
    }
}
