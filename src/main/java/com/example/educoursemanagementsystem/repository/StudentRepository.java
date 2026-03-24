package com.example.educoursemanagementsystem.repository;

import com.example.educoursemanagementsystem.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student,Long> {

    Boolean existsByEmail(String email);

    Optional<Student> findByEmail(String email);

    List<Student> findByIsActive(Boolean isActive);

    Optional<Student> deleteStudentById(Long id);



}
