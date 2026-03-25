package com.example.educoursemanagementsystem.controller;

import com.example.educoursemanagementsystem.model.dto.request.CourseRequestDTO;
import com.example.educoursemanagementsystem.model.dto.response.CourseDetailsResponseDTO;
import com.example.educoursemanagementsystem.model.dto.response.CourseResponseDTO;
import com.example.educoursemanagementsystem.service.CourseService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/courses")
public class CourseController {
    private final CourseService courseService;


    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping()
    public ResponseEntity<CourseResponseDTO> createCourse(@Valid @RequestBody CourseRequestDTO courseRequestDTO){
        CourseResponseDTO response=courseService.create(courseRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);


    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<CourseDetailsResponseDTO> getCourseById(@PathVariable Long id){
        CourseDetailsResponseDTO response=courseService.getById(id);
        return ResponseEntity.status(HttpStatus.OK).body(response);

    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping()
    public ResponseEntity<?> getAllCourse(){
        List<CourseResponseDTO> response=courseService.getAll();
        if(response.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course is not found");
        }
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/active")
    public ResponseEntity<List<CourseResponseDTO>> getAllActiveCourse(){
        List<CourseResponseDTO> response=courseService.getAllActiveCourse();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

   @GetMapping("/search/{title}")
   public ResponseEntity<?> searchByTitle(@PathVariable String title){
        List<CourseResponseDTO> response=courseService.searchByTitle(title);
        if (response.isEmpty()){
           return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course not found");
        }
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateCourse(@PathVariable Long id,@Valid @RequestBody CourseRequestDTO courseRequestDTO){
      courseService.update(id,courseRequestDTO);
        return ResponseEntity.status(HttpStatus.OK).body("Course updated.");
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteCourse(@PathVariable Long id) {
        courseService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/hardDelete/{id}")
    public ResponseEntity<?> hardDeleteCourse(@PathVariable Long id){
        courseService.hardDelete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/teacher/{teacher_id}")
    public ResponseEntity<?> getCoursesByTeacher(@PathVariable Long teacher_id){

       List<CourseDetailsResponseDTO> response= courseService.getCoursesByTeacher(teacher_id);
        return ResponseEntity.status(HttpStatus.OK).body(response);



    }



}
