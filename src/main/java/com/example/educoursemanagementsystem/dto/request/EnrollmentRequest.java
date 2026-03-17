package com.example.educoursemanagementsystem.dto.request;


import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EnrollmentRequest {
    @NotNull(message = "Student ID is required")
    Long studentId;

    @NotNull(message = "Course ID is required")
    Long courseId;
}
