package com.example.educoursemanagementsystem.repository;

import com.example.educoursemanagementsystem.model.entity.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LessonRepository extends JpaRepository<Lesson,Long> {
    List<Lesson> findByIsActive(Boolean isActive);

    Optional<Lesson> deleteLessonById(Long id);

    List<Lesson> findByTitleContainingIgnoreCase(String title);

}
