package com.example.educoursemanagementsystem.repository;

import com.example.educoursemanagementsystem.entity.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TeacherRepository extends JpaRepository<Teacher,Long> {
    Optional<Teacher> getAllActiveTeachers();
    Optional<Teacher> deleteTeacherById(Long id);
    Optional<Teacher> getTeachersByEmail(String email);
    List<Teacher> getTeachersByName(String name);
}
