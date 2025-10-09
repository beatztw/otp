package ru.chugunov.otp.model;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "check_otp")
public class CheckOtp extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String process_id;

    private String otp;

    private Timestamp check_time;

    private boolean isCorrect;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CheckOtp checkOtp = (CheckOtp) o;
        return Objects.equals(id, checkOtp.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
