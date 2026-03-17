package com.example.educoursemanagementsystem.dto.response;


import com.example.educoursemanagementsystem.enums.EnrollmentStatus;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EnrollmentResponse {

    @NotNull
    @Positive
    Long id;

    @NotNull
    @Positive
    Long studentId;

    String studentName;

    String studentSurname;

    @NotNull
    @Positive
    Long courseId;

    String courseTitle;

    LocalDate enrollmentDate;

    EnrollmentStatus status;

    LocalDateTime createAt;

    LocalDateTime updateAt;

    Boolean isActive;


}
