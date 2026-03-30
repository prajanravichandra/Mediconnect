package com.hospital.mediconnect.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class GoogleLoginRequest {
    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String name;
    
    // In a real scenario, you'd pass a credential token provided by Google to verify.
    // private String token; 
}
