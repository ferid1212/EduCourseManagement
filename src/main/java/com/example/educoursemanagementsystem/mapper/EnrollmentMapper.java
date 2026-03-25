package com.example.educoursemanagementsystem.mapper;

import com.example.educoursemanagementsystem.model.dto.response.EnrollmentResponse;
import com.example.educoursemanagementsystem.model.entity.Enrollment;

public interface EnrollmentMapper {
    EnrollmentResponse toEnrollmentResponse(Enrollment enrollment);
}
