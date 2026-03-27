package com.example.educoursemanagementsystem.model.dto.response;

import jakarta.validation.constraints.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TeacherResponse {
    @NotNull(message = "ID is required")
    @Positive(message = "ID must be positive")
    Long id;
    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 50, message = "Name must be between 2 and 50 characters")
    @Pattern(regexp = "^[a-zA-Z\\s]+$", message = "Name can only contain letters and spaces")
    String name;
    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    @Size(max = 100, message = "Email cannot exceed 100 characters")
    String email;
    @NotBlank(message = "Surname is required")
    @Size(min = 2, max = 50, message = "Surname must be between 2 and 50 characters")
    @Pattern(regexp = "^[a-zA-Z\\s]+$", message = "Surname can only contain letters and spaces")
    String surname;
    @NotNull(message = "Age is required")
    @Min(value = 18, message = "Age must be at least 18")
    @Max(value = 100, message = "Age cannot exceed 100")
    Integer age;
    @Pattern(regexp = "^994(50|51|55|99|70|77|10)\\d{7}$",message = "Please,Enter correct format for phone")
    String phone;
    LocalDateTime create_at;
    LocalDateTime update_at;
    @NotNull(message = "Active status is required")
    Boolean isActive;
    String courseName;

}
