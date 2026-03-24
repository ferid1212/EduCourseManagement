package com.example.educoursemanagementsystem.service;

import com.example.educoursemanagementsystem.dto.request.StudentRequest;
import com.example.educoursemanagementsystem.dto.response.StudentResponse;
import com.example.educoursemanagementsystem.entity.Student;
import com.example.educoursemanagementsystem.mapper.StudentMapper;
import com.example.educoursemanagementsystem.repository.StudentRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {
    private final StudentRepository studentRepository;
    private  final StudentMapper studentMapper;

    public StudentResponse createStudent(StudentRequest request){
        if(studentRepository.existsByEmail(request.getEmail())){
            throw new RuntimeException("Bu email artıq mövcuddur: " + request.getEmail());
        }

        Student student=Student.builder()
                .name(request.getName())
                .surname(request.getSurname())
                .age(request.getAge())
                .email(request.getEmail())
                .phone(request.getPhone())
                .build();

        Student saved=studentRepository.save(student);
        return studentMapper.toStudentResponse(saved);
    }

    public StudentResponse getStudentById(Long id){
        Student student=studentRepository.findById(id).orElseThrow(()->new RuntimeException("Student not found"));
        return studentMapper.toStudentResponse(student);
    }

    public List<StudentResponse> getAllStudents(){
       return studentRepository.findAll().stream()
               .map(studentMapper :: toStudentResponse)
               .toList();

    }

    public List<StudentResponse> getAllActiveStudents(){
       return studentRepository.findByIsActive(true).stream()
               .map(studentMapper :: toStudentResponse)
               .toList();

    }

    public void updateStudent(Long id,StudentRequest request){
        Student student=studentRepository.findById(id).orElseThrow(()->new RuntimeException("Student not found"));
        student.setName(request.getName());
        student.setSurname(request.getSurname());
        student.setAge(request.getAge());
        student.setEmail(request.getEmail());
        student.setPhone(request.getPhone());

        Student updated=studentRepository.save(student);
        studentMapper.toStudentResponse(updated);
    }

    public void softDeleteStudent(Long id){
        Student student=studentRepository.findById(id).orElseThrow(()->new RuntimeException("Student not found"));
        student.setIsActive(false);
        studentRepository.save(student);

    }

    public void hardDeleteStudent(Long id){
        Student student = studentRepository.findById(id).orElseThrow(()->new RuntimeException("Student not found"));
        studentRepository.delete(student);
    }



}
