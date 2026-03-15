package com.example.educoursemanagementsystem.service;


import com.example.educoursemanagementsystem.dto.request.TeacherRequest;
import com.example.educoursemanagementsystem.dto.response.TeacherResponse;
import com.example.educoursemanagementsystem.entity.Course;
import com.example.educoursemanagementsystem.entity.Teacher;
import com.example.educoursemanagementsystem.mapper.TeacherMapper;
import com.example.educoursemanagementsystem.repository.CourseRepository;
import com.example.educoursemanagementsystem.repository.TeacherRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class TeacherServiceImpl implements TeacherService{

    private final TeacherRepository teacherRepository;
    private final CourseRepository courseRepository;
    private final TeacherMapper teacherMapper;


    @Override
    public TeacherResponse createTeacher(TeacherRequest request) {

        if (teacherRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Bu email artıq mövcuddur: " + request.getEmail());
        }


        Course course = null;
        if (request.getCourseId() != null) {
            course = courseRepository.findById(request.getCourseId())
                    .orElseThrow(() -> new RuntimeException("Kurs tapılmadı: " + request.getCourseId()));
        }


        Teacher teacher = Teacher.builder()
                .name(request.getName())
                .surname(request.getSurname())
                .age(request.getAge())
                .email(request.getEmail())
                .course(course)
                .build();


        Teacher saved = teacherRepository.save(teacher);
        return teacherMapper.toTeacherResponse(saved);
    }

    @Override
    public TeacherResponse getTeacherById(Long id) {
        Teacher teacher=teacherRepository.findById(id).orElseThrow(()->new RuntimeException("Teacher not found"));
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
        Teacher teacher=teacherRepository.findById(id).orElseThrow(()->new RuntimeException("Teacher not found."));
        teacher.setName(request.getName());
        teacher.setSurname(request.getSurname());
        teacher.setAge(request.getAge());
        teacher.setEmail(request.getEmail());
        Teacher updated=teacherRepository.save(teacher);
        teacherMapper.toTeacherResponse(updated);

    }

    @Override
    public void softDeleteTeacher(Long id) {
        Teacher teacher=teacherRepository.findById(id).orElseThrow(()->new RuntimeException("Teacher not found."));
        teacher.setIsActive(false);
        teacherRepository.save(teacher);
    }

    @Override
    public void hardDeleteTeacher(Long id) {
        Teacher teacher=teacherRepository.deleteTeacherById(id).orElseThrow(()->new RuntimeException("Teacher not found."));


    }

    @Override
    public TeacherResponse getTeacherByEmail(String email) {
        Teacher teacher=teacherRepository.getTeachersByEmail(email).orElseThrow(()->new RuntimeException("This email is not found"));
        return teacherMapper.toTeacherResponse(teacher);
    }

    @Override
    public List<TeacherResponse> searchTeachersByName(String name) {
        List<TeacherResponse> teachers=teacherRepository.getTeachersByName(name).stream()
                .map(teacherMapper :: toTeacherResponse)
                .toList();

        if (teachers.isEmpty()){
            throw new RuntimeException("Name is not found");
        }

         return  teachers;



    }


}
