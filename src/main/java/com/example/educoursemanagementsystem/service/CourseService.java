package com.example.educoursemanagementsystem.service;

import com.example.educoursemanagementsystem.dto.response.CourseDetailsResponseDTO;
import com.example.educoursemanagementsystem.dto.request.CourseRequestDTO;
import com.example.educoursemanagementsystem.dto.response.CourseResponseDTO;

import java.util.List;

public interface CourseService {
    CourseResponseDTO create(CourseRequestDTO courseRequestDTO);
    void delete (Long id);
    CourseDetailsResponseDTO getById(Long id);
    List<CourseResponseDTO> getAll();
    List<CourseResponseDTO> searchByTitle(String title);
    CourseResponseDTO update(Long id,CourseRequestDTO courseRequestDTO);


}
