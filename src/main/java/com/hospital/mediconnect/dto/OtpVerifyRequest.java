package com.hospital.mediconnect.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class OtpVerifyRequest {
    @NotBlank
    private String email;

    @NotBlank
    private String otpCode;
}
