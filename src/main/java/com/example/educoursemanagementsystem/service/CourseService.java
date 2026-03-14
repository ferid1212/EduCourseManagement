package com.example.educoursemanagementsystem.service;

import com.example.educoursemanagementsystem.dto.response.CourseDetailsResponseDTO;
import com.example.educoursemanagementsystem.dto.request.CourseRequestDTO;
import com.example.educoursemanagementsystem.dto.response.CourseResponseDTO;

import java.util.List;

public interface CourseService {
    CourseResponseDTO create(CourseRequestDTO courseRequestDTO);

    void delete (Long id);

    void hardDelete(Long id);

    CourseDetailsResponseDTO getById(Long id);

    List<CourseResponseDTO> getAll();

    List<CourseResponseDTO> getAllActiveCourse();

    List<CourseResponseDTO> searchByTitle(String title);

    void update(Long id,CourseRequestDTO courseRequestDTO);

    //Business method

    List<CourseDetailsResponseDTO> getCoursesByTeacher(Long teacherId);




}
