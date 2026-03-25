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
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column(nullable = false)
    String name;
    @Column(nullable = false)
    String surname;
    Integer age;
    @Column(unique = true, nullable = false)
    String email;
    String phone;
    @Column(name = "create_at")
    @CreationTimestamp
    LocalDateTime createAt;
    @Column(name = "update_at")
    @UpdateTimestamp
    LocalDateTime updateAt;
    @Column(name = "is_active")
    Boolean isActive=true;

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    List<Enrollment> enrollments = new ArrayList<>();


    @PrePersist
    public void prePersist() {
        if (isActive == null) {
            isActive = true;
        }
    }







}
