package com.example.educoursemanagementsystem.mapper;

import com.example.educoursemanagementsystem.model.dto.response.TeacherResponse;
import com.example.educoursemanagementsystem.model.entity.Teacher;
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
                .phone(teacher.getPhone())
                .email(teacher.getEmail())
                .create_at(teacher.getCreateAt())
                .update_at(teacher.getUpdateAt())
                .isActive(teacher.getIsActive())
                .courseName(teacher.getCourse().getTitle())
                .build();
    }
}
