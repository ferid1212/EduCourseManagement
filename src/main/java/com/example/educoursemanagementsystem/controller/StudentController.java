package com.example.educoursemanagementsystem.controller;

import com.example.educoursemanagementsystem.dto.request.StudentRequest;
import com.example.educoursemanagementsystem.dto.response.StudentResponse;
import com.example.educoursemanagementsystem.service.StudentService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/students")
public class StudentController {

     private final StudentService studentService;

    @PreAuthorize("hasRole('ADMIN')")
     @PostMapping
     public ResponseEntity<StudentResponse> createStudent(@Valid @RequestBody StudentRequest studentRequest){
         StudentResponse response=studentService.createStudent(studentRequest);
         return  ResponseEntity.status(HttpStatus.CREATED).body(response);

     }

    @PreAuthorize("hasRole('ADMIN')")
     @GetMapping("/{id}")
    public ResponseEntity<StudentResponse> getStudentById(@PathVariable Long id){
         StudentResponse response=studentService.getStudentById(id);
         return  ResponseEntity.status(HttpStatus.OK).body(response);
     }

    @PreAuthorize("hasRole('ADMIN')")
     @GetMapping
    public ResponseEntity<List<StudentResponse>> getAllStudents(){
         List<StudentResponse> responses=studentService.getAllStudents();
         return ResponseEntity.status(HttpStatus.OK).body(responses);
     }

    @PreAuthorize("hasRole('ADMIN')")
     @GetMapping("/active")
    public ResponseEntity<List<StudentResponse>> getAllActiveStudents(){
         List<StudentResponse> responses=studentService.getAllActiveStudents();
         return ResponseEntity.status(HttpStatus.OK).body(responses);

     }

    @PreAuthorize("hasRole('ADMIN')")
     @PutMapping("/{id}")
    public void updatedStudent(@PathVariable Long id,@Valid @RequestBody StudentRequest request){
         studentService.updateStudent(id,request);
         ResponseEntity.status(HttpStatus.OK).body("Student updated");

     }

    @PreAuthorize("hasRole('ADMIN')")
     @DeleteMapping("/{id}")
    public void softDeleteStudent(@PathVariable Long id){
         studentService.softDeleteStudent(id);
         ResponseEntity.status(HttpStatus.OK).body("Student deleted");

     }
    @PreAuthorize("hasRole('ADMIN')")
     @DeleteMapping("/hard/{id}")
     public void hardDeleteStudent(@PathVariable Long id){
         studentService.hardDeleteStudent(id);
         ResponseEntity.status(HttpStatus.OK).body("Student deleted");
     }

}
