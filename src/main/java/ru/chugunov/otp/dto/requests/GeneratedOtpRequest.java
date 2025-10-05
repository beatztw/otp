package ru.chugunov.otp.dto.requests;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;
import ru.chugunov.otp.controllers.enums.SendingChannel;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GeneratedOtpRequest {

    @NotNull(message = "Идентификатор процесса не может быть пустым")
    private UUID processID;

    @NotNull(message = "Канал отправки не может быть пустым")
    private SendingChannel sendingChannel;

    @NotBlank(message = "Адресс выполнения отправки не может быть пустым")
    private String target;

    @NotBlank(message = "Текст сообщения не может быть пустым")
    private String message;

    @NotNull(message = "Длина одноразового пароля не может null")
    @Range(min = 4L, max = 8L, message = "Длина одноразового пароля не может быть меньше 4 и больше 8")
    private Integer length;

    @NotNull(message = "Время жизни одноразового пароля не может nNull")
    @Min(value = 30L, message = "Время жизни одноразового пароля не может быть меньше 30 секунд")
    private Integer ttl;

    @NotNull(message = "Время жизни сессии одноразового пароля не может null")
    @Min(value = 60L, message = "Время жизни сессии одноразового пароля не может быть меньше 60")
    private Integer sessionTtl;

    @NotNull(message = "Количество повторных запросов на отправку не может null")
    @Range(min = 1L, max = 3L, message = "Количество повторных запросов на отправку не может быть меньше 1 и больше 3")
    private Integer resendAttempts;

    @NotNull(message = "Таймаут повторной отправки не может null")
    @Min(value = 30L, message = "Таймаут повторной отправки не может быть меньше 30 секунд")
    private Integer resendTimeout;
}
