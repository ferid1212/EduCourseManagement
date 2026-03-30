package com.example.educoursemanagementsystem.repository;

import com.example.educoursemanagementsystem.model.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CourseRepository extends JpaRepository<Course,Long> {
    List<Course> findByTitleContainingIgnoreCase(String title);
    Optional<Course> findByTitle(String title);

    @Query("SELECT c FROM Course c WHERE c.isActive = :isActive OR (:isActive = true AND c.isActive IS NULL)")
    List<Course> findByIsActive(Boolean isActive);

    Optional<Course> deleteCourseById(Long id);

    Optional<Course> getCourseByTeachersId(Long teacherId);










}
