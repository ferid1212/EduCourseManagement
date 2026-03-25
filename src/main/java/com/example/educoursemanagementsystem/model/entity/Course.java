package com.example.educoursemanagementsystem.model.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter

@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column(nullable = false)
    String title;
    @Column(length = 2000)
    String description;
    Integer duration;
    Double price;
    @Column(name = "create_at")
    @CreationTimestamp
    LocalDateTime createAt;
    @Column(name = "update_at")
    @UpdateTimestamp
    LocalDateTime updateAt;
    @Column(name = "is_active")
    Boolean isActive=true;


    @OneToMany(mappedBy = "course")

    List<Teacher> teachers;

    @OneToMany(mappedBy = "course")

    List<Lesson> lessons = new ArrayList<>();

    @OneToMany(mappedBy = "course")

    List<Enrollment> enrollments = new ArrayList<>();


    @PrePersist
    public void prePersist() {
        if (isActive == null) {
            isActive = true;
        }
    }

}
