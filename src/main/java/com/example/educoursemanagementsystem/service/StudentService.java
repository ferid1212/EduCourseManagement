package com.example.educoursemanagementsystem.service;

import com.example.educoursemanagementsystem.model.dto.request.StudentRequest;
import com.example.educoursemanagementsystem.model.dto.response.StudentResponse;

import java.util.List;

public interface StudentService {
    StudentResponse createStudent(StudentRequest request);
    StudentResponse getStudentById(Long id);
    List<StudentResponse> getAllStudents();
    List<StudentResponse> getAllActiveStudents();
    void updateStudent(Long id, StudentRequest request);
    void softDeleteStudent(Long id);
    void hardDeleteStudent(Long id);
}
