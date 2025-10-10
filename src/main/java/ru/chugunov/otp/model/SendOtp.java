package ru.chugunov.otp.model;

import jakarta.persistence.*;
import lombok.*;
import ru.chugunov.otp.controllers.enums.SendOtpStatus;
import ru.chugunov.otp.controllers.enums.SendingChannel;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(indexes = {
        @Index(name = "idx_send_otp_process_id", columnList = "process_id"),
        @Index(name = "idx_send_otp_target_status", columnList = "target, status")
})
public class SendOtp extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String processId;

    @Enumerated(EnumType.STRING)
    private SendingChannel sendingChannel;

    private String target;

    private String message;

    private Integer length;

    private Integer ttl;

    private Integer sessionTtl;

    private Integer resendAttempts;

    private Integer resendTimeout;

    private String encodedOtp;

    private String sendMessageKey;

    @Enumerated(EnumType.STRING)
    private SendOtpStatus status;

    private LocalDateTime sendTime;


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

