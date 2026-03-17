package com.example.educoursemanagementsystem.controller;


import com.example.educoursemanagementsystem.dto.request.EnrollmentRequest;
import com.example.educoursemanagementsystem.dto.response.EnrollmentResponse;
import com.example.educoursemanagementsystem.enums.EnrollmentStatus;
import com.example.educoursemanagementsystem.service.EnrollmentService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/enrollments")
public class EnrollmentController {

    private final EnrollmentService enrollmentService;

    @PostMapping
    public ResponseEntity<EnrollmentResponse> createEnrollment(
            @Valid @RequestBody EnrollmentRequest request) {
        EnrollmentResponse response = enrollmentService.enrollStudent(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EnrollmentResponse> getEnrollmentById(@PathVariable Long id) {
        EnrollmentResponse response = enrollmentService.getEnrollmentById(id);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping
    public ResponseEntity<List<EnrollmentResponse>> getAllEnrollments() {
        List<EnrollmentResponse> responses = enrollmentService.getAllEnrollments();
        return ResponseEntity.status(HttpStatus.OK).body(responses);
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<EnrollmentResponse>> getEnrollmentsByStudent(
            @PathVariable Long studentId) {
        List<EnrollmentResponse> responses = enrollmentService.getEnrollmentsByStudent(studentId);
        return ResponseEntity.status(HttpStatus.OK).body(responses);
    }

    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<EnrollmentResponse>> getEnrollmentsByCourse(
            @PathVariable Long courseId) {
        List<EnrollmentResponse> responses = enrollmentService.getEnrollmentsByCourse(courseId);
        return ResponseEntity.status(HttpStatus.OK).body(responses);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<EnrollmentResponse>> getEnrollmentsByStatus(
            @PathVariable EnrollmentStatus status) {
        List<EnrollmentResponse> responses = enrollmentService.getEnrollmentsByStatus(status);
        return ResponseEntity.status(HttpStatus.OK).body(responses);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<EnrollmentResponse> updateStatus(
            @PathVariable Long id,
            @RequestParam EnrollmentStatus status) {
        EnrollmentResponse response = enrollmentService.updateStatus(id, status);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping("/{id}/complete")
    public ResponseEntity<EnrollmentResponse> completeEnrollment(@PathVariable Long id) {
        EnrollmentResponse response = enrollmentService.completeEnrollment(id);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> softDeleteEnrollment(@PathVariable Long id) {
        enrollmentService.softDeleteEnrollment(id);
        return ResponseEntity.status(HttpStatus.OK).body("Enrollment cancelled successfully");
    }

    @DeleteMapping("/hard/{id}")
    public ResponseEntity<String> hardDeleteEnrollment(@PathVariable Long id) {
        enrollmentService.hardDeleteEnrollment(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/course/{courseId}/count")
    public ResponseEntity<Long> getActiveStudentCount(@PathVariable Long courseId) {
        long count = enrollmentService.getActiveStudentCountInCourse(courseId);
        return ResponseEntity.status(HttpStatus.OK).body(count);
    }

    @GetMapping("/check")
    public ResponseEntity<Boolean> checkEnrollment(
            @RequestParam Long studentId,
            @RequestParam Long courseId) {
        boolean enrolled = enrollmentService.isStudentEnrolledInCourse(studentId, courseId);
        return ResponseEntity.status(HttpStatus.OK).body(enrolled);
    }
}
