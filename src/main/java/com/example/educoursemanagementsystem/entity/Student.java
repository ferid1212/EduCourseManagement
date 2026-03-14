package com.example.educoursemanagementsystem.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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
    @Column(name = "registration_date")
    LocalDate registrationDate;
    @Column(name = "create_at")
    LocalDateTime createAt;
    @Column(name = "update_at")
    LocalDateTime updateAt;
    @Column(name = "is_active")
    Boolean isActive=true;

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    List<Enrollment> enrollments = new ArrayList<>();







}
