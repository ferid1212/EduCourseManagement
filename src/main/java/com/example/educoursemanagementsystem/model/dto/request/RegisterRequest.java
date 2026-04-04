package com.example.educoursemanagementsystem.model.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class RegisterRequest {
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    @Email
    private String email;
    @Size(min = 6, message = "Password ən azı 6 simvol olmalıdır")
    private String password;
    private Long courseId;
    @NotNull(message = "Age is required")
    @Min(value = 18, message = "Age must be at least 18")
    @Max(value = 100, message = "Age cannot exceed 100")
    private Integer age;
    @Pattern(regexp = "^994(50|51|55|99|70|77|10)\\d{7}$",message = "Please,Enter correct format for phone")
    private String phone;
}
