package com.hospital.mediconnect.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import java.util.Date;

@Data
public class SignupRequest {
    @NotBlank
    private String name;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String password;

    private String phone;
    private String role;
    private Long hospitalId;
    private String specialization;
    private String availabilityStatus;
    private Date dateOfBirth;
    private String gender;
    private String bloodGroup;
    private String address;
    private String emergencyContact;
}
