package com.example.educoursemanagementsystem.controller;

import com.example.educoursemanagementsystem.dto.request.StudentRequest;
import com.example.educoursemanagementsystem.dto.response.StudentResponse;
import com.example.educoursemanagementsystem.service.StudentService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/students")
public class StudentController {

     private final StudentService studentService;

     @PostMapping
     public ResponseEntity<StudentResponse> createStudent(@Valid @RequestBody StudentRequest studentRequest){
         StudentResponse response=studentService.createStudent(studentRequest);
         return  ResponseEntity.status(HttpStatus.CREATED).body(response);

     }

     @GetMapping("/{id}")
    public ResponseEntity<StudentResponse> getStudentById(@PathVariable Long id){
         StudentResponse response=studentService.getStudentById(id);
         return  ResponseEntity.status(HttpStatus.OK).body(response);
     }

     @GetMapping
    public ResponseEntity<List<StudentResponse>> getAllStudents(){
         List<StudentResponse> responses=studentService.getAllStudents();
         return ResponseEntity.status(HttpStatus.OK).body(responses);
     }

     @GetMapping("/active")
    public ResponseEntity<List<StudentResponse>> getAllActiveStudents(){
         List<StudentResponse> responses=studentService.getAllActiveStudents();
         return ResponseEntity.status(HttpStatus.OK).body(responses);

     }

     @PutMapping("/{id}")
    public void updatedStudent(@PathVariable Long id,@Valid @RequestBody StudentRequest request){
         studentService.updateStudent(id,request);
         ResponseEntity.status(HttpStatus.OK).body("Student updated");

     }

     @DeleteMapping("/{id}")
    public void softDeleteStudent(@PathVariable Long id){
         studentService.softDeleteStudent(id);
         ResponseEntity.status(HttpStatus.OK).body("Student deleted");

     }
     @DeleteMapping("/hard/{id}")
     public void hardDeleteStudent(@PathVariable Long id){
         studentService.hardDeleteStudent(id);
         ResponseEntity.status(HttpStatus.OK).body("Student deleted");
     }

}
