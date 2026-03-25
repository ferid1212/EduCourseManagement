package com.example.educoursemanagementsystem.repository;

import com.example.educoursemanagementsystem.model.entity.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TeacherRepository extends JpaRepository<Teacher,Long> {
    List<Teacher> findByIsActive(Boolean isActive);
    Optional<Teacher> deleteTeacherById(Long id);
    Optional<Teacher> getTeachersByEmail(String email);
    List<Teacher> getTeachersByName(String name);

    boolean existsByEmail(String email);

}
