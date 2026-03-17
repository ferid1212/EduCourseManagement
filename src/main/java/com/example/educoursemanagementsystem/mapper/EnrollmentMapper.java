package com.example.educoursemanagementsystem.mapper;

import com.example.educoursemanagementsystem.dto.response.EnrollmentResponse;
import com.example.educoursemanagementsystem.entity.Enrollment;

public interface EnrollmentMapper {
    EnrollmentResponse toEnrollmentResponse(Enrollment enrollment);
}
