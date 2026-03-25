package com.example.educoursemanagementsystem.model.dto.request;


import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EnrollmentRequest {
    Long studentId;

    Long courseId;

    String courseName;
}
