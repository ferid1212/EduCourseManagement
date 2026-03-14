package com.example.educoursemanagementsystem.controller;


import com.example.educoursemanagementsystem.dto.request.TeacherRequest;
import com.example.educoursemanagementsystem.dto.response.TeacherResponse;
import com.example.educoursemanagementsystem.service.TeacherService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/teachers")
public class TeacherController {

    private final TeacherService teacherService;

    @PostMapping
    public ResponseEntity<TeacherResponse> createTeacher(@Valid @RequestBody TeacherRequest teacherRequest){
       TeacherResponse response= teacherService.createTeacher(teacherRequest);
       return ResponseEntity.status(HttpStatus.CREATED).body(response);

    }

}
