package com.example.educoursemanagementsystem.service;

import com.example.educoursemanagementsystem.dto.response.CourseDetailsResponseDTO;
import com.example.educoursemanagementsystem.dto.request.CourseRequestDTO;
import com.example.educoursemanagementsystem.dto.response.CourseResponseDTO;
import com.example.educoursemanagementsystem.entity.Course;
import com.example.educoursemanagementsystem.mapper.EntityMapper;
import com.example.educoursemanagementsystem.repository.CourseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;
    private final EntityMapper mapper;

    @Override
    public CourseResponseDTO create(CourseRequestDTO courseRequestDTO) {
        Course course=Course.builder()
                .title(courseRequestDTO.getTitle())
                .description(courseRequestDTO.getDescription())
                .duration(courseRequestDTO.getDuration())
                .price(courseRequestDTO.getPrice())
                .build();

        Course saved=courseRepository.save(course);
        return mapper.toCourseResponseDTO(saved);



    }

    @Override
    public void delete(Long id) {
        Course course=courseRepository.findById(id).orElseThrow(()->new RuntimeException("Course not found."));
        course.setIsActive(false);
        courseRepository.save(course);
    }

    @Override
    public CourseDetailsResponseDTO getById(Long id) {
        Course course=courseRepository.findById(id).orElseThrow(()->new RuntimeException("Course not found."));
        return mapper.toCourseDetailsResponseDTO(course);
    }

    @Override
    public List<CourseResponseDTO> getAll() {
        return courseRepository.findAll().stream()
                .map(mapper::toCourseResponseDTO)
                .toList();
    }

    @Override
    public List<CourseResponseDTO> searchByTitle(String title) {
        return courseRepository.findByTitleIgnoreCase(title).stream()
                .map(mapper::toCourseResponseDTO)
                .toList();
    }

    @Override
    public CourseResponseDTO update(Long id, CourseRequestDTO courseRequestDTO) {
        Course course=courseRepository.findById(id).orElseThrow(()-> new RuntimeException("Course not found."));
        course.setTitle(courseRequestDTO.getTitle());
        course.setDescription(courseRequestDTO.getDescription());
        course.setDuration(courseRequestDTO.getDuration());
        course.setPrice(courseRequestDTO.getPrice());

        Course updated=courseRepository.save(course);
        return  mapper.toCourseResponseDTO(updated);
    }


}
