package ru.chugunov.otp.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class CheckOtp extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    /**
     * Идентификатор процесса в рамках которого запрашивается одноразовый пароль
     */
    private String processId;
    /**
     * Введенный одноразовый пароль
     */
    private String otp;
    /**
     * Время проверки одноразового пароля
     */
    private LocalDateTime checkTime;
    /**
     * Признак корректности одноразового пароля
     */
    private Boolean correct;

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
