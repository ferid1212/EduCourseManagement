package com.example.educoursemanagementsystem.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Entity
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String title;

    @ManyToMany(mappedBy = "courses")
    private Set<Student> students;

    @ManyToMany(mappedBy = "courses")
    private Set<Teacher> teachers;
}
