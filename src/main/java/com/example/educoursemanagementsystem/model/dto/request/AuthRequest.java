package com.example.educoursemanagementsystem.model.dto.request;

import jakarta.validation.constraints.Email;
import lombok.Data;

@Data
public class AuthRequest {
    @Email
    private String email;
    private String password;
}
