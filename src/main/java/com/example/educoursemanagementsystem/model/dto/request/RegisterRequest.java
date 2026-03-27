package com.example.educoursemanagementsystem.model.dto.request;

import lombok.Data;

@Data
public class RegisterRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private Long courseId;
    private Integer age;
    private String phone;
}
