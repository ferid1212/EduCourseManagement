package com.example.educoursemanagementsystem.controller;


import com.example.educoursemanagementsystem.model.dto.request.EnrollmentRequest;
import com.example.educoursemanagementsystem.model.dto.response.EnrollmentResponse;
import com.example.educoursemanagementsystem.enums.EnrollmentStatus;
import com.example.educoursemanagementsystem.service.EnrollmentService;
import com.example.educoursemanagementsystem.repository.UserRepository;
import com.example.educoursemanagementsystem.repository.StudentRepository;
import com.example.educoursemanagementsystem.repository.CourseRepository;
import com.example.educoursemanagementsystem.model.entity.Course;
import com.example.educoursemanagementsystem.model.entity.User;
import com.example.educoursemanagementsystem.model.entity.Student;
import org.springframework.security.core.context.SecurityContextHolder;
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
    private final UserRepository userRepository;
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;

    @PostMapping
    public ResponseEntity<EnrollmentResponse> createEnrollment(
            @Valid @RequestBody EnrollmentRequest request) {
            
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Logged in user not found in DB"));
                
        Student student = studentRepository.findByEmail(email).orElse(null);
        if (student == null) {
            student = new Student();
            student.setName(user.getFirstName());
            student.setSurname(user.getLastName());
            student.setEmail(user.getEmail());
            student.setPhone(user.getPhone());
            student.setIsActive(true);
            student = studentRepository.save(student);
        }
        
        request.setStudentId(student.getId());

        if (request.getCourseName() != null && !request.getCourseName().trim().isEmpty()) {
            Course course = courseRepository.findByTitle(request.getCourseName())
                    .orElseThrow(() -> new RuntimeException("Bu adda kurs tapılmadı: " + request.getCourseName()));
            request.setCourseId(course.getId());
        }

        if (request.getCourseId() == null) {
            throw new RuntimeException("Kursa yazılmaq üçün 'courseName' və ya 'courseId' qeyd edilməlidir!");
        }

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
