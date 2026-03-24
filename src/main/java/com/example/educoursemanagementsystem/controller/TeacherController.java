package com.example.educoursemanagementsystem.controller;


import com.example.educoursemanagementsystem.dto.request.TeacherRequest;
import com.example.educoursemanagementsystem.dto.response.TeacherResponse;
import com.example.educoursemanagementsystem.entity.Teacher;
import com.example.educoursemanagementsystem.service.TeacherService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/teachers")
public class TeacherController {

    private final TeacherService teacherService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<TeacherResponse> createTeacher(@Valid @RequestBody TeacherRequest teacherRequest){
       TeacherResponse response= teacherService.createTeacher(teacherRequest);
       return ResponseEntity.status(HttpStatus.CREATED).body(response);

    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<TeacherResponse> getTeacherById(@PathVariable Long id){
        TeacherResponse response=teacherService.getTeacherById(id);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<TeacherResponse>> getAllTeachers(){
        List<TeacherResponse> teacherResponses=teacherService.getAllTeachers();
        return ResponseEntity.status(HttpStatus.OK).body(teacherResponses);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/active")
    public ResponseEntity<List<TeacherResponse>> getAllActiveTeachers(){
        List<TeacherResponse> teacherResponses=teacherService.getAllActiveTeachers();
        return ResponseEntity.status(HttpStatus.OK).body(teacherResponses);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/email/{email}")
    public ResponseEntity<TeacherResponse> getTeacherByEmail(@PathVariable String email){
        TeacherResponse response=teacherService.getTeacherByEmail(email);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/name/{name}")
    public ResponseEntity<List<TeacherResponse>> searchTeachersByName(@PathVariable String name){
        List<TeacherResponse> responses=teacherService.searchTeachersByName(name);
        return ResponseEntity.status(HttpStatus.OK).body(responses);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public void updateTeacher(@PathVariable Long id,@Valid @RequestBody TeacherRequest request){
       teacherService.updateTeacher(id,request);
       ResponseEntity.status(HttpStatus.OK).body("Teacher updated");
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public void softDeleteTeacher(@PathVariable Long id){
        teacherService.softDeleteTeacher(id);
        ResponseEntity.status(HttpStatus.OK).body("Teacher deleted");
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete/{id}")
    public void hardDeleteTeacher(@PathVariable Long id){
        teacherService.hardDeleteTeacher(id);
        ResponseEntity.status(HttpStatus.OK).body("Teacher deleted");
    }



}
