package com.example.educoursemanagementsystem.model.entity;

import com.example.educoursemanagementsystem.enums.EnrollmentStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "enrollments",
        uniqueConstraints = @UniqueConstraint(columnNames = {"student_id", "course_id"}))
public class Enrollment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    Student student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    Course course;

    @Column(name = "enrollment_date")
    LocalDate enrollmentDate;



    @Column(name = "create_at")
    @CreationTimestamp
    LocalDateTime createAt;

    @Column(name = "update_at")
    @UpdateTimestamp
    LocalDateTime updateAt;

    @Column(name = "is_active")
    @Builder.Default
    Boolean isActive = true;


    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.VARCHAR)
    @Column(name = "status", nullable = false, length = 32)
    @Builder.Default
    EnrollmentStatus status = EnrollmentStatus.ACTIVE;

    @Column(name = "is_paid")
    @Builder.Default
    Boolean isPaid = false;

    @PrePersist
    public void prePersist() {
        if (isActive == null) {
            isActive = true;
        }
        if (status == null) {
            status = EnrollmentStatus.ACTIVE;
        }
        if (isPaid == null) {
            isPaid = false;
        }
        if (enrollmentDate == null) {
            enrollmentDate = LocalDate.now();
        }
    }
}
