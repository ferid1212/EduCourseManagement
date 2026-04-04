package com.example.educoursemanagementsystem.service.impl;

import com.example.educoursemanagementsystem.exception.ResourceNotFoundException;
import com.example.educoursemanagementsystem.model.dto.response.CourseDetailsResponseDTO;
import com.example.educoursemanagementsystem.model.dto.request.CourseRequestDTO;
import com.example.educoursemanagementsystem.model.dto.response.CourseResponseDTO;
import com.example.educoursemanagementsystem.model.entity.Course;
import com.example.educoursemanagementsystem.model.entity.Teacher;
import com.example.educoursemanagementsystem.mapper.CourseMapper;
import com.example.educoursemanagementsystem.repository.CourseRepository;
import com.example.educoursemanagementsystem.repository.TeacherRepository;
import com.example.educoursemanagementsystem.service.CourseService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;
    private final CourseMapper mapper;
    private final TeacherRepository teacherRepository;

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
        Course course=courseRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Course not found."));
        course.setIsActive(false);
        courseRepository.save(course);
    }

    @Override
    public void hardDelete(Long id) {
        Course course = courseRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Kurs tapılmadı."));
        courseRepository.delete(course);
    }

    @Override
    public CourseDetailsResponseDTO getById(Long id) {
        Course course=courseRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Course not found."));
        return mapper.toCourseDetailsResponseDTO(course);
    }

    @Override
    public List<CourseResponseDTO> getAll() {
        return courseRepository.findAll().stream()
                .map(mapper::toCourseResponseDTO)
                .toList();
    }

    @Override
    public List<CourseResponseDTO> getAllActiveCourse() {
        return courseRepository.findByIsActive(true).stream()
                .map(mapper::toCourseResponseDTO)
                .toList();
    }

    @Override
    public List<CourseResponseDTO> searchByTitle(String title) {
        return courseRepository.findByTitleContainingIgnoreCase(title).stream()
                .map(mapper::toCourseResponseDTO)
                .toList();
    }

    @Override
    public void update(Long id, CourseRequestDTO courseRequestDTO) {
        Course course=courseRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Kurs tapılmadı."));
        course.setTitle(courseRequestDTO.getTitle());
        course.setDescription(courseRequestDTO.getDescription());
        course.setDuration(courseRequestDTO.getDuration());
        course.setPrice(courseRequestDTO.getPrice());
        courseRepository.save(course);
    }

    @Override
    public List<CourseDetailsResponseDTO> getCoursesByTeacher(Long teacherId) {
        Teacher teacher=teacherRepository.findById(teacherId).orElseThrow(()->new ResourceNotFoundException("Teacher is not found."));
        Course course = teacher.getCourse();
        if (course == null) {
            return List.of();
        }

        return List.of(mapper.toCourseDetailsResponseDTO(course));
    }



}
