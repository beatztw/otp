package ru.chugunov.otp.dto.requests;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GeneratedOtpRequest {

    @NotEmpty(message = "Идентификатор процесса не может быть пустым")
    private UUID processID;
    @NotBlank(message = "Канал отправки не может быть пустым")
    @Pattern(regexp = "^telegram|console$", message = "Допустимые значения telegram или console")
    private String sendingChannel;
    @NotBlank(message = "Адресс выполнения отправки не может быть пустым")
    private String target;
    @NotBlank(message = "Текст сообщения не может быть пустым")
    private String message;
    @NotEmpty(message = "Длина одноразового пароля не может быть пустым")
    @Min(value = 4L, message = "Длина одноразового пароля не может быть меньше 4")
    @Max(value = 8L, message = "Длина одноразового пароля не может быть больше 8")
    private long length;
    @NotEmpty(message = "Время жизни одноразового пароля не может быть пустым")
    @Min(value = 30L, message = "Время жизни одноразового пароля не может быть меньше 30 секунд")
    private long ttl;
    @NotEmpty(message = "Время жизни сессии одноразового пароля не может быть пустым")
    @Min(value = 60L, message = "Время жизни сессии одноразового пароля не может быть меньше 60")
    private long sessionTtl;
    @NotEmpty(message = "Количество повторных запросов на отправку не может быть пустым")
    @Min(value = 1L, message = "Количество повторных запросов на отправку не может быть меньше 1")
    @Max(value = 3L, message = "Количество повторных запросов на отправку не может быть больше 3")
    private long resendAttempts;
    @NotEmpty(message = "Таймаут повторной отправки не может быть пустым")
    @Min(value = 30L, message = "Таймаут повторной отправки не может быть меньше 30 секунд")
    private long resendTimeout;
}
