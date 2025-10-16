package ru.chugunov.otp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.chugunov.otp.model.CheckOtp;

import java.util.UUID;

public interface CheckOtpRepository extends JpaRepository<CheckOtp, UUID> {

    boolean existsByProcessIdAndOtpAndCorrectTrue(String processId, String otp);

}
