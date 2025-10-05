package ru.chugunov.otp.dto.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CheckOtpRequest {

    @NotNull(message = "Идентификатор процесса не может быть пустым")
    private UUID processID;

    @NotBlank(message = "Одноразовый пароль не может быть пустым")
    private String otp;
}
