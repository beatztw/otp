package ru.chugunov.otp.dto.responses;

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

    private SendOtpKafkaStatus status;

    private String errorMessage;
}
