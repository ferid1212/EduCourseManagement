package com.example.educoursemanagementsystem.mapper;

import com.example.educoursemanagementsystem.dto.response.CourseResponseDTO;
import com.example.educoursemanagementsystem.dto.response.TeacherResponse;
import com.example.educoursemanagementsystem.entity.Teacher;
import org.springframework.stereotype.Component;

@Component
public class TeacherMapperImpl implements TeacherMapper{

    @Override
    public TeacherResponse toTeacherResponse(Teacher teacher) {
        if(teacher==null){
            return null;
        }
        return TeacherResponse.builder()
                .id(teacher.getId())
                .name(teacher.getName())
                .surname(teacher.getSurname())
                .age(teacher.getAge())
                .email(teacher.getEmail())
                .create_at(teacher.getCreateAt())
                .update_at(teacher.getUpdateAt())
                .isActive(teacher.getIsActive())
                .build();
    }
}
