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
public class SendOtp extends AuditableEntity {

    /**
     * Уникальный идентификатор записи
     */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    /**
     * Идентификатор процесса в рамках которого запрашивается одноразовый пароль
     */
    private String processId;
    /**
     * Канал отправки одноразового пароля
     */
    @Enumerated(EnumType.STRING)
    private SendingChannel sendingChannel;
    /**
     * Адрес, куда будет выполнена отправка в начале
     */
    private String target;
    /**
     * Текст сообщения отправки
     */
    private String message;
    /**
     * Длина одноразового пароля
     */
    private Integer length;
    /**
     * Время жизни одноразового пароля (в сек.)
     */
    private Integer ttl;
    /**
     * Время жизни сессии одноразового пароля (в сек.)
     */
    private Integer sessionTtl;
    /**
     * Количество возможных повторных отправок кода
     */
    private Integer resendAttempts;
    /**
     * Таймаут перед повторным запросом кода (в сек.)
     */
    private Integer resendTimeout;
    /**
     * Зашифрованный одноразовый пароль
     */
    private String encodedOtp;
    /**
     * Идентификатор сообщения, отправляемого во внешнюю систему
     */
    private String sendMessageKey;
    /**
     * Статус отправки сообщения
     */
    @Enumerated(EnumType.STRING)
    private SendOtpStatus status;
    /**
     * Время отправки одноразового пароля
     */
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

