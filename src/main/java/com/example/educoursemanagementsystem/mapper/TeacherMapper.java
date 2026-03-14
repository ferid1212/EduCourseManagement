package com.example.educoursemanagementsystem.mapper;

import com.example.educoursemanagementsystem.dto.response.CourseDetailsResponseDTO;
import com.example.educoursemanagementsystem.dto.response.CourseResponseDTO;
import com.example.educoursemanagementsystem.dto.response.TeacherResponse;
import com.example.educoursemanagementsystem.entity.Course;
import com.example.educoursemanagementsystem.entity.Teacher;

public interface TeacherMapper {
    TeacherResponse toTeacherResponse(Teacher teacher);

}
