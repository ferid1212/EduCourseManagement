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
import com.example.educoursemanagementsystem.exception.BadRequestException;
import com.example.educoursemanagementsystem.exception.ResourceNotFoundException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

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
                .orElseThrow(() -> new ResourceNotFoundException("Logged in user not found in DB"));
                
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
                    .orElseThrow(() -> new ResourceNotFoundException("Course with this name not found: " + request.getCourseName()));
            request.setCourseId(course.getId());
        }

        if (request.getCourseId() == null) {
            throw new BadRequestException("Either 'courseName' or 'courseId' must be provided for enrollment!");
        }

        EnrollmentResponse response = enrollmentService.enrollStudent(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EnrollmentResponse> getEnrollmentById(@PathVariable Long id) {
        EnrollmentResponse response = enrollmentService.getEnrollmentById(id);
        if (isStudentOnlyUser()) {
            Long sid = currentUserStudentIdOrThrow();
            if (!Objects.equals(response.getStudentId(), sid)) {
                throw new AccessDeniedException("You do not have permission to view this enrollment.");
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping
    public ResponseEntity<List<EnrollmentResponse>> getAllEnrollments() {
        if (isStudentOnlyUser()) {
            Optional<Long> sid = currentUserStudentId();
            if (sid.isEmpty()) {
                return ResponseEntity.status(HttpStatus.OK).body(List.of());
            }
            List<EnrollmentResponse> responses = enrollmentService.getEnrollmentsByStudent(sid.get());
            return ResponseEntity.status(HttpStatus.OK).body(responses);
        }
        List<EnrollmentResponse> responses = enrollmentService.getAllEnrollments();
        return ResponseEntity.status(HttpStatus.OK).body(responses);
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<EnrollmentResponse>> getEnrollmentsByStudent(
            @PathVariable Long studentId) {
        if (isStudentOnlyUser()) {
            Long sid = currentUserStudentIdOrThrow();
            if (!studentId.equals(sid)) {
                throw new AccessDeniedException("You can only see your own enrollments.");
            }
        }
        List<EnrollmentResponse> responses = enrollmentService.getEnrollmentsByStudent(studentId);
        return ResponseEntity.status(HttpStatus.OK).body(responses);
    }

    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<EnrollmentResponse>> getEnrollmentsByCourse(
            @PathVariable Long courseId) {
        if (isStudentOnlyUser()) {
            throw new AccessDeniedException("You do not have permission to view this list.");
        }
        List<EnrollmentResponse> responses = enrollmentService.getEnrollmentsByCourse(courseId);
        return ResponseEntity.status(HttpStatus.OK).body(responses);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<EnrollmentResponse>> getEnrollmentsByStatus(
            @PathVariable EnrollmentStatus status) {
        if (isStudentOnlyUser()) {
            throw new AccessDeniedException("You do not have permission to view this list.");
        }
        List<EnrollmentResponse> responses = enrollmentService.getEnrollmentsByStatus(status);
        return ResponseEntity.status(HttpStatus.OK).body(responses);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<EnrollmentResponse> updateStatus(
            @PathVariable Long id,
            @RequestParam EnrollmentStatus status) {
        if (isStudentOnlyUser()) {
            throw new AccessDeniedException("You do not have permission to change the status.");
        }
        EnrollmentResponse response = enrollmentService.updateStatus(id, status);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping("/{id}/complete")
    public ResponseEntity<EnrollmentResponse> completeEnrollment(@PathVariable Long id) {
        if (isStudentOnlyUser()) {
            throw new AccessDeniedException("You do not have permission to complete the enrollment.");
        }
        EnrollmentResponse response = enrollmentService.completeEnrollment(id);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping("/{id}/pay")
    public ResponseEntity<EnrollmentResponse> payEnrollment(@PathVariable Long id) {
        // Students can only pay for their own enrollments
        if (isStudentOnlyUser()) {
            Long studentId = currentUserStudentIdOrThrow();
            EnrollmentResponse enrollment = enrollmentService.getEnrollmentById(id);
            if (!Objects.equals(enrollment.getStudentId(), studentId)) {
                throw new AccessDeniedException("You can only pay for your own enrollment.");
            }
        }
        EnrollmentResponse response = enrollmentService.payEnrollment(id);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> softDeleteEnrollment(@PathVariable Long id) {
        Long restrictToStudent = isStudentOnlyUser() ? currentUserStudentIdOrThrow() : null;
        enrollmentService.cancelEnrollment(id, restrictToStudent);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/hard/{id}")
    public ResponseEntity<String> hardDeleteEnrollment(@PathVariable Long id) {
        if (isStudentOnlyUser()) {
            throw new AccessDeniedException("You do not have permission for this operation.");
        }
        enrollmentService.hardDeleteEnrollment(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/course/{courseId}/count")
    public ResponseEntity<Long> getActiveStudentCount(@PathVariable Long courseId) {
        if (isStudentOnlyUser()) {
            throw new AccessDeniedException("You do not have permission to view this information.");
        }
        long count = enrollmentService.getActiveStudentCountInCourse(courseId);
        return ResponseEntity.status(HttpStatus.OK).body(count);
    }

    @GetMapping("/check")
    public ResponseEntity<Boolean> checkEnrollment(
            @RequestParam Long studentId,
            @RequestParam Long courseId) {
        if (isStudentOnlyUser()) {
            Long sid = currentUserStudentIdOrThrow();
            if (!studentId.equals(sid)) {
                throw new AccessDeniedException("You can only check your own enrollment.");
            }
        }
        boolean enrolled = enrollmentService.isStudentEnrolledInCourse(studentId, courseId);
        return ResponseEntity.status(HttpStatus.OK).body(enrolled);
    }

    private boolean isStudentOnlyUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) {
            return false;
        }
        Set<String> roles = auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());
        return roles.contains("ROLE_STUDENT")
                && !roles.contains("ROLE_ADMIN")
                && !roles.contains("ROLE_TEACHER");
    }

    private Optional<Long> currentUserStudentId() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return studentRepository.findByEmail(email).map(Student::getId);
    }

    private Long currentUserStudentIdOrThrow() {
        return currentUserStudentId()
                .orElseThrow(() -> new AccessDeniedException("Student profile not found."));
    }
}
