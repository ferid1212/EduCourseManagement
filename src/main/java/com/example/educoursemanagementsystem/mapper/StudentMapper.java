package com.example.educoursemanagementsystem.mapper;

import com.example.educoursemanagementsystem.model.dto.response.StudentResponse;
import com.example.educoursemanagementsystem.model.entity.Student;

public interface StudentMapper {

    StudentResponse toStudentResponse(Student student);
}
