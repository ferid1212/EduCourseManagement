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
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;
    private Long courseId;
    @NotNull(message = "Age is required")
    @Min(value = 18, message = "Age must be at least 18")
    @Max(value = 100, message = "Age cannot exceed 100")
    private Integer age;
    @Pattern(
            regexp = "^994(50|51|55|99|70|77|10)[0-9]{7}$",
            message = "Phone number is not in the correct format (Example: 99450XXXXXXX)"
    )
    private String phone;
}
