package ru.chugunov.otp.dto.responses;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.chugunov.otp.controllers.enums.SendOtpKafkaStatus;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SendOtpKafkaResponse implements Serializable {

    private String id;
    @NotNull(message = "Статус ответа обязателен")
    private SendOtpKafkaStatus status;

    private String errorMessage;
}
