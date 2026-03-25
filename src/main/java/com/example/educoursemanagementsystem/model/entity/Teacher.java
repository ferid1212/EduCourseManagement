package com.example.educoursemanagementsystem.model.entity;


import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Teacher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column(nullable = false)
    String name;
    @Column(unique = true, nullable = false)
    String email;
    @Column(nullable = false)
    String surname;
    Integer age;
    @Column(name = "create_at")
    @CreationTimestamp
    LocalDateTime createAt;
    @Column(name = "update_at")
    @UpdateTimestamp
    LocalDateTime updateAt;
    @Column(name = "is_active")
    Boolean isActive=true;



    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id")
    Course course;


    @PrePersist
    public void prePersist() {
        if (isActive == null) {
            isActive = true;
        }
    }

}
