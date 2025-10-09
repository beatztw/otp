package ru.chugunov.otp.model;

import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import static ru.chugunov.otp.utils.Constants.DEFAULT_DB_USER;

@Getter
@Setter
@MappedSuperclass
public class AuditableEntity {

    private Timestamp createTime;
    private String createUser;
    private Timestamp lastUpdateTime;
    private String lastUpdateUser;

    @PrePersist
    public void prePersist() {
        Timestamp now = Timestamp.valueOf(LocalDateTime.now());
        this.createTime = now;
        this.lastUpdateTime = now;
        this.createUser = DEFAULT_DB_USER;
        this.lastUpdateUser = DEFAULT_DB_USER;
    }

    @PreUpdate
    public void PreUpdate() {
        this.lastUpdateTime = Timestamp.valueOf(LocalDateTime.now());
        this.lastUpdateUser = DEFAULT_DB_USER;
    }
}
