package com.example.educoursemanagementsystem.repository;

import com.example.educoursemanagementsystem.model.entity.Enrollment;
import com.example.educoursemanagementsystem.enums.EnrollmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment,Long> {

    // Tələbənin bütün qeydiyyatları
    List<Enrollment> findByStudentIdAndIsActiveTrue(Long studentId);

    // Kursun bütün qeydiyyatları
    List<Enrollment> findByCourseIdAndIsActiveTrue(Long courseId);

    // Statusa görə axtarış
    List<Enrollment> findByStatusAndIsActiveTrue(EnrollmentStatus status);

    // Tələbə və kursa görə unikal qeydiyyat
    Optional<Enrollment> findByStudentIdAndCourseId(Long studentId, Long courseId);

    // Aktiv qeydiyyat yoxlaması
    boolean existsByStudentIdAndCourseIdAndIsActiveTrue(Long studentId, Long courseId);

    // Kursda aktiv tələbə sayı
    long countByCourseIdAndStatusAndIsActiveTrue(Long courseId, EnrollmentStatus status);

    // Tələbənin aktiv kurs sayı
    long countByStudentIdAndStatusAndIsActiveTrue(Long studentId, EnrollmentStatus status);

    @Query("SELECT e FROM Enrollment e " +
            "JOIN FETCH e.student " +
            "JOIN FETCH e.course " +
            "WHERE e.id = :id")
    Optional<Enrollment> findByIdWithDetails(@Param("id") Long id);

    @Query("SELECT e FROM Enrollment e " +
            "WHERE e.student.id = :studentId " +
            "AND e.status = :status " +
            "AND e.isActive = true")
    List<Enrollment> findByStudentAndStatus(@Param("studentId") Long studentId,
                                            @Param("status") EnrollmentStatus status);

}
