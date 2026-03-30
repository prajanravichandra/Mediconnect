package com.hospital.mediconnect.controller;

import com.hospital.mediconnect.dto.*;
import com.hospital.mediconnect.model.User;
import com.hospital.mediconnect.repository.UserRepository;
import com.hospital.mediconnect.util.HashUtil;
import com.hospital.mediconnect.service.OtpService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*", maxAge = 3600)
public class AuthController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    OtpService otpService;

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Email is already in use!"));
        }

        User user = new User();
        user.setName(signUpRequest.getName());
        user.setEmail(signUpRequest.getEmail());
        user.setPassword(HashUtil.hashPassword(signUpRequest.getPassword()));
        user.setPhone(signUpRequest.getPhone());
        user.setRole(signUpRequest.getRole() != null ? signUpRequest.getRole() : "patient");
        user.setHospitalId(signUpRequest.getHospitalId());
        user.setSpecialization(signUpRequest.getSpecialization());
        user.setAvailabilityStatus(signUpRequest.getAvailabilityStatus());
        user.setDateOfBirth(signUpRequest.getDateOfBirth());
        user.setGender(signUpRequest.getGender());
        user.setBloodGroup(signUpRequest.getBloodGroup());
        user.setAddress(signUpRequest.getAddress());
        user.setEmergencyContact(signUpRequest.getEmergencyContact());

        User savedUser = userRepository.save(user);

        return ResponseEntity.ok(new AuthResponse("User registered successfully!", savedUser));
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        User user = userRepository.findByEmail(loginRequest.getEmail()).orElse(null);

        if (user == null || !HashUtil.checkPassword(loginRequest.getPassword(), user.getPassword())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Invalid email or password!"));
        }

        return ResponseEntity.ok(new AuthResponse("Login successful", user));
    }

    @PostMapping("/otp/request")
    public ResponseEntity<?> requestOtp(@RequestParam String email) {
        if (!userRepository.existsByEmail(email)) {
             return ResponseEntity.badRequest().body(new MessageResponse("Error: User Email not found!"));
        }
        otpService.generateAndSendOtp(email);
        return ResponseEntity.ok(new MessageResponse("OTP sent successfully to " + email));
    }

    @PostMapping("/otp/verify")
    public ResponseEntity<?> verifyOtpAndLogin(@Valid @RequestBody OtpVerifyRequest otpRequest) {
        boolean isValid = otpService.validateOtp(otpRequest.getEmail(), otpRequest.getOtpCode());
        
        if (isValid) {
            User user = userRepository.findByEmail(otpRequest.getEmail()).orElseThrow(() -> new RuntimeException("User not found"));
            return ResponseEntity.ok(new AuthResponse("OTP verified, login successful", user));
        }
        return ResponseEntity.badRequest().body(new MessageResponse("Error: Invalid or Expired OTP"));
    }

    @PostMapping("/oauth2/google")
    public ResponseEntity<?> googleLogin(@Valid @RequestBody GoogleLoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail()).orElse(null);
        
        if (user == null) {
            user = new User();
            user.setEmail(request.getEmail());
            user.setName(request.getName());
            user.setRole("patient");
            user = userRepository.save(user);
        }

        return ResponseEntity.ok(new AuthResponse("Google login successful", user));
    }
}
