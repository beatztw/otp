package ru.chugunov.otp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.chugunov.otp.model.SendOtp;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SendOtpRepository extends JpaRepository<SendOtp, UUID> {

    @Query("""
    FROM SendOtp c
    WHERE c.processId = :processId
    ORDER BY c.createTime DESC
    LIMIT 1
    """)
    Optional<SendOtp> findFirstByProcessIdAndOrderByCreateTimeDesc(String processId);

    List<SendOtp> findAllByProcessIdOrderByCreateTimeAsc(String processId);
}
