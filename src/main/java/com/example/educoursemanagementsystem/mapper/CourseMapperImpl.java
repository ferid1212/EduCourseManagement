package com.example.educoursemanagementsystem.mapper;

import com.example.educoursemanagementsystem.model.dto.response.CourseDetailsResponseDTO;
import com.example.educoursemanagementsystem.model.dto.response.CourseResponseDTO;
import com.example.educoursemanagementsystem.model.dto.response.TeacherResponse;
import com.example.educoursemanagementsystem.model.entity.Course;
import org.springframework.stereotype.Component;


@Component
public class CourseMapperImpl implements CourseMapper {
    @Override
    public CourseResponseDTO toCourseResponseDTO(Course course) {
        if(course==null){
            return null;
        }
        return CourseResponseDTO.builder()
                .id(course.getId())
                .title(course.getTitle())
                .description(course.getDescription())
                .duration(course.getDuration())
                .price(course.getPrice())
                .create_at(course.getCreateAt())
                .update_at(course.getUpdateAt())
                .isActive(course.getIsActive())
                .build();
    }

    @Override
    public CourseDetailsResponseDTO toCourseDetailsResponseDTO(Course course) {
        if(course==null){
            return null;
        }
        return CourseDetailsResponseDTO.builder()
                .id(course.getId())
                .title(course.getTitle())
                .description(course.getDescription())
                .price(course.getPrice())
                .duration(course.getDuration())
                .create_at(course.getCreateAt())
                .update_at(course.getUpdateAt())
                .teachers(course.getTeachers().stream()
                        .map(teacher -> {
                            return TeacherResponse.builder()
                                    .id(teacher.getId())
                                    .name(teacher.getName())
                                    .surname(teacher.getSurname())
                                    .email(teacher.getEmail())
                                    .age(teacher.getAge())
                                    .create_at(teacher.getCreateAt())
                                    .update_at(teacher.getUpdateAt())
                                    .build();
                        }).toList())
                .build();
    }

}

