package ru.chugunov.otp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.chugunov.otp.model.SendOtp;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SendOtpRepository extends JpaRepository<SendOtp, UUID> {

    Optional<SendOtp> findFirstByProcessIdOrderByCreateTimeDesc(String processId);

    List<SendOtp> findAllByProcessIdOrderByCreateTimeAsc(String processId);
}
