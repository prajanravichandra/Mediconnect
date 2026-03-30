package com.hospital.mediconnect.dto;

import com.hospital.mediconnect.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthResponse {
    private String message;
    private User user;
}
