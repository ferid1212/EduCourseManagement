package com.example.educoursemanagementsystem.service.impl;


import com.example.educoursemanagementsystem.model.dto.request.EnrollmentRequest;
import com.example.educoursemanagementsystem.model.dto.response.EnrollmentResponse;
import com.example.educoursemanagementsystem.model.entity.Course;
import com.example.educoursemanagementsystem.model.entity.Enrollment;
import com.example.educoursemanagementsystem.model.entity.Student;
import com.example.educoursemanagementsystem.enums.EnrollmentStatus;
import com.example.educoursemanagementsystem.exception.AlreadyExistsException;
import com.example.educoursemanagementsystem.exception.ResourceNotFoundException;
import com.example.educoursemanagementsystem.mapper.EnrollmentMapper;
import com.example.educoursemanagementsystem.repository.CourseRepository;
import com.example.educoursemanagementsystem.repository.EnrollmentRepository;
import com.example.educoursemanagementsystem.repository.StudentRepository;
import com.example.educoursemanagementsystem.service.EnrollmentService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.security.access.AccessDeniedException;

@Service
@AllArgsConstructor
@Slf4j
public class EnrollmentServiceImpl implements EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;
    private final EnrollmentMapper enrollmentMapper;

    @Override
    @Transactional
    public EnrollmentResponse enrollStudent(EnrollmentRequest request) {
        Student student=studentRepository.findById(request.getStudentId()).orElseThrow(() -> new ResourceNotFoundException(
                "Student not found with id: " + request.getStudentId()));
        Course course=courseRepository.findById(request.getCourseId()).orElseThrow(() -> new ResourceNotFoundException(
                "Course not found with id: " + request.getCourseId()));

        if (!Boolean.TRUE.equals(course.getIsActive())) {
            throw new IllegalStateException("Cannot enroll in inactive course");
        }

        if (enrollmentRepository.existsByStudentIdAndCourseIdAndIsActiveTrue(
                request.getStudentId(), request.getCourseId())) {
            throw new AlreadyExistsException(
                    "Siz bu kursdan zatən qeydiyyatdan keçmisiz.");
        }

        Optional<Enrollment> previous = enrollmentRepository.findByStudentIdAndCourseId(
                request.getStudentId(), request.getCourseId());
        if (previous.isPresent()) {
            Enrollment existing = previous.get();
            if (Boolean.TRUE.equals(existing.getIsActive())) {
                throw new AlreadyExistsException(
                        "Siz bu kursdan zatən qeydiyyatdan keçmisiz.");
            }
            existing.setIsActive(true);
            existing.setStatus(EnrollmentStatus.PENDING);
            existing.setIsPaid(false);
            existing.setEnrollmentDate(LocalDate.now());
            Enrollment saved = enrollmentRepository.save(existing);
            log.info("Enrollment reactivated with id: {}", saved.getId());
            return enrollmentMapper.toEnrollmentResponse(saved);
        }

        Enrollment enrollment = Enrollment.builder()
                .student(student)
                .course(course)
                .status(EnrollmentStatus.PENDING)
                .isPaid(false)
                .enrollmentDate(LocalDate.now())
                .build();

        Enrollment savedEnrollment = enrollmentRepository.save(enrollment);
        log.info("Enrollment created with id: {}", savedEnrollment.getId());

        return enrollmentMapper.toEnrollmentResponse(savedEnrollment);
    }

    @Override
    public EnrollmentResponse getEnrollmentById(Long id) {
        Enrollment enrollment = enrollmentRepository.findByIdWithDetails(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Enrollment not found with id: " + id));
        return enrollmentMapper.toEnrollmentResponse(enrollment);
    }

    @Override
    public List<EnrollmentResponse> getAllEnrollments() {
        return enrollmentRepository.findAll().stream()
                .filter(e -> Boolean.TRUE.equals(e.getIsActive()))
                .map(enrollmentMapper::toEnrollmentResponse)
                .toList();
    }

    @Override
    public List<EnrollmentResponse> getEnrollmentsByStudent(Long studentId) {

        if (!studentRepository.existsById(studentId)) {
            throw new ResourceNotFoundException("Student not found with id: " + studentId);
        }

        return enrollmentRepository.findByStudentIdAndIsActiveTrue(studentId).stream()
                .map(enrollmentMapper::toEnrollmentResponse)
                .toList();

    }

    @Override
    public List<EnrollmentResponse> getEnrollmentsByCourse(Long courseId) {
        if (!courseRepository.existsById(courseId)) {
            throw new ResourceNotFoundException("Course not found with id: " + courseId);
        }

        return enrollmentRepository.findByCourseIdAndIsActiveTrue(courseId).stream()
                .map(enrollmentMapper::toEnrollmentResponse)
                .toList();
    }

    @Override
    public List<EnrollmentResponse> getEnrollmentsByStatus(EnrollmentStatus status) {
        return enrollmentRepository.findByStatusAndIsActiveTrue(status).stream()
                .map(enrollmentMapper::toEnrollmentResponse)
                .toList();
    }

    @Override
    public EnrollmentResponse updateStatus(Long id, EnrollmentStatus status) {
        log.info("Updating enrollment {} status to {}", id, status);

        Enrollment enrollment = enrollmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Enrollment not found with id: " + id));

        enrollment.setStatus(status);




        Enrollment updated = enrollmentRepository.save(enrollment);
        return enrollmentMapper.toEnrollmentResponse(updated);
    }

    @Override
    @Transactional
    public EnrollmentResponse completeEnrollment(Long id) {
        return updateStatus(id, EnrollmentStatus.COMPLETED);
    }

    @Override
    @Transactional
    public void cancelEnrollment(Long enrollmentId, Long studentIdIfRestricted) {
        log.info("Cancelling enrollment: {}", enrollmentId);
        Enrollment enrollment = enrollmentRepository.findByIdWithDetails(enrollmentId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Enrollment not found with id: " + enrollmentId));
        if (studentIdIfRestricted != null) {
            Long ownerId = enrollment.getStudent() != null ? enrollment.getStudent().getId() : null;
            if (!Objects.equals(ownerId, studentIdIfRestricted)) {
                throw new AccessDeniedException("Bu qeydiyyatı ləğv etmək üçün icazəniz yoxdur.");
            }
        }
        // DB-də enrollments_status_check CANCELLED qəbul etməyə bilər; ləğv üçün kifayətdir ki, siyahılarda is_active=false süzülür.
        enrollment.setIsActive(false);
        enrollmentRepository.save(enrollment);
    }

    @Override
    @Transactional
    public void softDeleteEnrollment(Long id) {
        cancelEnrollment(id, null);
    }

    @Override
    @Transactional
    public void hardDeleteEnrollment(Long id) {
        log.info("Hard deleting enrollment: {}", id);

        if (!enrollmentRepository.existsById(id)) {
            throw new ResourceNotFoundException("Enrollment not found with id: " + id);
        }

        enrollmentRepository.deleteById(id);

    }

    @Override
    @Transactional
    public EnrollmentResponse payEnrollment(Long enrollmentId) {
        log.info("Processing payment for enrollment: {}", enrollmentId);
        Enrollment enrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Enrollment not found: " + enrollmentId));
        
        enrollment.setIsPaid(true);
        enrollment.setStatus(EnrollmentStatus.ACTIVE);
        
        Enrollment saved = enrollmentRepository.save(enrollment);
        return enrollmentMapper.toEnrollmentResponse(saved);
    }

    @Override
    @Transactional
    public long getActiveStudentCountInCourse(Long courseId) {
        return enrollmentRepository.countByCourseIdAndStatusAndIsActiveTrue(
                courseId, EnrollmentStatus.ACTIVE);
    }

    @Override
    @Transactional
    public boolean isStudentEnrolledInCourse(Long studentId, Long courseId) {
        return enrollmentRepository.existsByStudentIdAndCourseIdAndIsActiveTrue(
                studentId, courseId);
    }
}
