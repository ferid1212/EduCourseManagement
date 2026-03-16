package com.example.educoursemanagementsystem.mapper;

import com.example.educoursemanagementsystem.dto.response.CourseDetailsResponseDTO;
import com.example.educoursemanagementsystem.dto.response.CourseResponseDTO;
import com.example.educoursemanagementsystem.entity.Course;
import org.springframework.stereotype.Component;


public interface CourseMapper {
    CourseResponseDTO toCourseResponseDTO(Course course);
    CourseDetailsResponseDTO toCourseDetailsResponseDTO(Course course);
}
