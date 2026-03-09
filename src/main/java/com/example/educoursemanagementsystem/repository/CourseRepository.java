package com.example.educoursemanagementsystem.repository;

import com.example.educoursemanagementsystem.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CourseRepository extends JpaRepository<Course,Long> {
    Optional<Course> findByTitleIgnoreCase(String title);






}
