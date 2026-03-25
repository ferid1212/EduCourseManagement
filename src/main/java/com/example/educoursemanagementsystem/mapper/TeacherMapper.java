package com.example.educoursemanagementsystem.mapper;

import com.example.educoursemanagementsystem.model.dto.response.TeacherResponse;
import com.example.educoursemanagementsystem.model.entity.Teacher;

public interface TeacherMapper {
    TeacherResponse toTeacherResponse(Teacher teacher);

}
