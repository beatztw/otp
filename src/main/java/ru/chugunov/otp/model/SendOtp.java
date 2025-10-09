package ru.chugunov.otp.model;

import jakarta.persistence.*;
import lombok.*;
import ru.chugunov.otp.controllers.enums.SendOtpStatus;
import ru.chugunov.otp.controllers.enums.SendingChannel;

import java.sql.Timestamp;
import java.util.Objects;
import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "send_otp", indexes = {
        @Index(name = "idx_send_otp_process_id", columnList = "process_id"),
        @Index(name = "idx_send_otp_target_status", columnList = "target, status")
})
public class SendOtp extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String process_id;

    @Enumerated(EnumType.STRING)
    private SendingChannel sending_channel;

    private String target;

    private String message;

    private Integer length;

    private Integer ttl;

    private Integer session_ttl;

    private Integer resend_attempts;

    private Integer resend_timeout;

    private String encoded_otp;

    private String send_message_key;

    @Enumerated(EnumType.STRING)
    private SendOtpStatus status;

    private Timestamp send_time;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SendOtp sendOtp = (SendOtp) o;
        return Objects.equals(id, sendOtp.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}

