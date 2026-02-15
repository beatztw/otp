package ru.chugunov.otp.dto.responses;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.chugunov.otp.model.SendOtpKafkaStatus;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SendOtpKafkaResponse implements Serializable {

    @NotEmpty(message = "Идентификатор ответа от внешнего сервиса не может быть пустым")
    private String id;
    @NotNull(message = "Статус ответа обязателен")
    private SendOtpKafkaStatus status;

    private String errorMessage;
}
