package com.example.educoursemanagementsystem.mapper;

import com.example.educoursemanagementsystem.model.dto.response.CourseDetailsResponseDTO;
import com.example.educoursemanagementsystem.model.dto.response.CourseResponseDTO;
import com.example.educoursemanagementsystem.model.entity.Course;


public interface CourseMapper {
    CourseResponseDTO toCourseResponseDTO(Course course);
    CourseDetailsResponseDTO toCourseDetailsResponseDTO(Course course);
}
