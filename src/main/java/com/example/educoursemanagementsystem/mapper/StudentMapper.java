package com.example.educoursemanagementsystem.mapper;

import com.example.educoursemanagementsystem.dto.response.StudentResponse;
import com.example.educoursemanagementsystem.entity.Student;

public interface StudentMapper {

    StudentResponse toStudentResponse(Student student);
}
