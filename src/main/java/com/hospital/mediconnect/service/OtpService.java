package com.hospital.mediconnect.service;

import com.hospital.mediconnect.model.OtpEntity;
import com.hospital.mediconnect.repository.OtpRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Optional;
import java.util.Random;

@Service
public class OtpService {

    @Autowired
    private OtpRepository otpRepository;

    @Autowired
    private EmailService emailService;

    private static final long OTP_VALID_DURATION = 5 * 60 * 1000; // 5 minutes

    @Transactional
    public void generateAndSendOtp(String email) {
        otpRepository.deleteByEmail(email);

        String otp = String.format("%06d", new Random().nextInt(999999));

        OtpEntity otpEntity = new OtpEntity();
        otpEntity.setEmail(email);
        otpEntity.setOtpCode(otp);
        otpEntity.setExpirationTime(new Date(System.currentTimeMillis() + OTP_VALID_DURATION));
        
        otpRepository.save(otpEntity);

        String text = "Your OTP for Mediconnect Login is: " + otp + "\nIt is valid for 5 minutes.";
        emailService.sendSimpleMessage(email, "Mediconnect Login OTP", text);
    }

    @Transactional
    public boolean validateOtp(String email, String otpCode) {
        Optional<OtpEntity> otpEntityOptional = otpRepository.findByEmailAndOtpCode(email, otpCode);
        
        if (otpEntityOptional.isPresent()) {
            OtpEntity otpEntity = otpEntityOptional.get();
            if (otpEntity.getExpirationTime().after(new Date())) {
                otpRepository.deleteByEmail(email); // clear after successful use
                return true;
            }
            otpRepository.deleteByEmail(email); // clear expired
        }
        return false;
    }
}
