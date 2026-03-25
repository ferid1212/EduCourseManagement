package com.example.educoursemanagementsystem.service;

import com.example.educoursemanagementsystem.model.dto.request.EnrollmentRequest;
import com.example.educoursemanagementsystem.model.dto.response.EnrollmentResponse;
import com.example.educoursemanagementsystem.enums.EnrollmentStatus;

import java.util.List;

public interface EnrollmentService {

    EnrollmentResponse enrollStudent(EnrollmentRequest request);

    EnrollmentResponse getEnrollmentById(Long id);

    List<EnrollmentResponse> getAllEnrollments();

    List<EnrollmentResponse> getEnrollmentsByStudent(Long studentId);

    List<EnrollmentResponse> getEnrollmentsByCourse(Long courseId);

    List<EnrollmentResponse> getEnrollmentsByStatus(EnrollmentStatus status);

    EnrollmentResponse updateStatus(Long id, EnrollmentStatus status);

    EnrollmentResponse completeEnrollment(Long id);

    void softDeleteEnrollment(Long id);

    void hardDeleteEnrollment(Long id);

    long getActiveStudentCountInCourse(Long courseId);

    boolean isStudentEnrolledInCourse(Long studentId, Long courseId);
}
