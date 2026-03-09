package com.example.educoursemanagementsystem.entity;

import com.example.educoursemanagementsystem.enums.EnrollmentStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Enrollment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne
    @JoinColumn(name = "student_id")
     Student student;

    @ManyToOne
    @JoinColumn(name = "course_id")
    Course course;

    LocalDate enrollmentDate;


    @Enumerated(EnumType.STRING)
    EnrollmentStatus status;
}
