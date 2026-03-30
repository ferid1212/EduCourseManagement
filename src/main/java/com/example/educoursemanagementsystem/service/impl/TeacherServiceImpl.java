package com.example.educoursemanagementsystem.service.impl;


import com.example.educoursemanagementsystem.exception.AlreadyExistsException;
import com.example.educoursemanagementsystem.exception.ResourceNotFoundException;
import com.example.educoursemanagementsystem.model.dto.request.TeacherRequest;
import com.example.educoursemanagementsystem.model.dto.response.TeacherResponse;
import com.example.educoursemanagementsystem.model.entity.Course;
import com.example.educoursemanagementsystem.model.entity.Teacher;
import com.example.educoursemanagementsystem.mapper.TeacherMapper;
import com.example.educoursemanagementsystem.repository.CourseRepository;
import com.example.educoursemanagementsystem.repository.TeacherRepository;
import com.example.educoursemanagementsystem.service.TeacherService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class TeacherServiceImpl implements TeacherService {

    private final TeacherRepository teacherRepository;
    private final CourseRepository courseRepository;
    private final TeacherMapper teacherMapper;


    @Override
    public TeacherResponse createTeacher(TeacherRequest request) {

        if (teacherRepository.existsByEmail(request.getEmail())) {
            throw new AlreadyExistsException("Bu email artıq mövcuddur: " + request.getEmail());
        }


        Course course = null;
        if (request.getCourseId() != null) {
            course = courseRepository.findById(request.getCourseId())
                    .orElseThrow(() -> new ResourceNotFoundException("Kurs tapılmadı: " + request.getCourseId()));
        }


        Teacher teacher = Teacher.builder()
                .name(request.getName())
                .surname(request.getSurname())
                .age(request.getAge())
                .phone(request.getPhone())
                .email(request.getEmail())
                .course(course)
                .build();


        Teacher saved = teacherRepository.save(teacher);
        return teacherMapper.toTeacherResponse(saved);
    }

    @Override
    public TeacherResponse getTeacherById(Long id) {
        Teacher teacher=teacherRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Teacher not found"));
        return teacherMapper.toTeacherResponse(teacher);

    }

    @Override
    public List<TeacherResponse> getAllTeachers() {
        return teacherRepository.findAll().stream()
                .map(teacherMapper::toTeacherResponse)
                .toList();

    }

    @Override
    public List<TeacherResponse> getAllActiveTeachers() {

        return teacherRepository.findByIsActive(true).stream()
                 .map(teacherMapper :: toTeacherResponse )
                 .toList();
    }

    @Override
    public void updateTeacher(Long id, TeacherRequest request) {
        Teacher teacher=teacherRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Teacher not found."));
        teacher.setName(request.getName());
        teacher.setSurname(request.getSurname());
        teacher.setAge(request.getAge());
        teacher.setPhone(request.getPhone());
        teacher.setEmail(request.getEmail());

        if (request.getCourseId() != null) {
            courseRepository.findById(request.getCourseId())
                    .ifPresent(teacher::setCourse);
        }

        Teacher updated=teacherRepository.save(teacher);
        teacherMapper.toTeacherResponse(updated);

    }

    @Override
    public void softDeleteTeacher(Long id) {
        Teacher teacher=teacherRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Teacher not found."));
        teacher.setIsActive(false);
        teacherRepository.save(teacher);
    }

    @Override
    public void hardDeleteTeacher(Long id) {
        Teacher teacher = teacherRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Teacher not found."));
        teacherRepository.delete(teacher);
    }

    @Override
    public TeacherResponse getTeacherByEmail(String email) {
        Teacher teacher=teacherRepository.getTeachersByEmail(email).orElseThrow(()->new ResourceNotFoundException("This email is not found"));
        return teacherMapper.toTeacherResponse(teacher);
    }

    @Override
    public List<TeacherResponse> searchTeachersByName(String name) {
        return teacherRepository.findByNameContainingIgnoreCaseOrSurnameContainingIgnoreCase(name, name).stream()
                .map(teacherMapper::toTeacherResponse)
                .toList();
    }


}
