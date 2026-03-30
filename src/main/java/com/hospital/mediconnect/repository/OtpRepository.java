package com.hospital.mediconnect.repository;

import com.hospital.mediconnect.model.OtpEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OtpRepository extends JpaRepository<OtpEntity, Long> {
    Optional<OtpEntity> findByEmailAndOtpCode(String email, String otpCode);
    void deleteByEmail(String email);
}
