package com.example.educoursemanagementsystem.mapper;


import com.example.educoursemanagementsystem.dto.response.StudentResponse;
import com.example.educoursemanagementsystem.entity.Student;
import org.springframework.stereotype.Component;

@Component
public class StudentMapperImpl implements StudentMapper{
    @Override
    public StudentResponse toStudentResponse(Student student) {
        if(student==null){
            return null;
        }
        return StudentResponse.builder()
                .id(student.getId())
                .name(student.getName())
                .surname(student.getSurname())
                .age(student.getAge())
                .email(student.getEmail())
                .phone(student.getPhone())
                .create_at(student.getCreateAt())
                .update_at(student.getUpdateAt())
                .isActive(student.getIsActive())
                .build();
    }
}
