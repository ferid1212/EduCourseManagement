package com.example.educoursemanagementsystem.controller;


import com.example.educoursemanagementsystem.model.dto.request.LessonRequest;
import com.example.educoursemanagementsystem.model.dto.response.LessonResponse;
import com.example.educoursemanagementsystem.service.LessonService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/lessons")
public class LessonController {

    private final LessonService lessonService;

    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER')")
    @PostMapping()
    public ResponseEntity<LessonResponse> createLesson( @Valid @RequestBody LessonRequest lessonRequest){
        LessonResponse response=lessonService.createLesson(lessonRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<LessonResponse> getLessonById(@PathVariable Long id){
        LessonResponse response=lessonService.getLessonById(id);
        return ResponseEntity.status(HttpStatus.OK).body(response);

    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<LessonResponse>> getAllLessons(){
        List<LessonResponse> responses=lessonService.getAllLessons();
        return ResponseEntity.status(HttpStatus.OK).body(responses);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER')")
    @GetMapping("/active")
    public ResponseEntity<List<LessonResponse>> getAllActiveLessons(){
        List<LessonResponse> responses=lessonService.getAllActiveLessons();
        return ResponseEntity.status(HttpStatus.OK).body(responses);

    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER')")
    @GetMapping("/title/{title}")
    public ResponseEntity<List<LessonResponse>> searchLessonByTitle(@PathVariable String title){
        List<LessonResponse> responses=lessonService.searchLessonsByTitle(title);
        return ResponseEntity.status(HttpStatus.OK).body(responses);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<LessonResponse> updateLesson(@PathVariable Long id,@Valid @RequestBody LessonRequest request){
        LessonResponse response=lessonService.updateLesson(id,request);
        return ResponseEntity.status(HttpStatus.OK).body(response);

    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/video/{id}/{videoURL}")
    public ResponseEntity<LessonResponse> updateVideoURL(@PathVariable Long id,
                                                         @PathVariable String videoURL){
        LessonResponse response=lessonService.updateVideoUrl(id,videoURL);
        return ResponseEntity.status(HttpStatus.OK).body(response);

    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public void softDeleteLesson(@PathVariable Long id){
        lessonService.softDeleteLesson(id);
        ResponseEntity.status(HttpStatus.OK).body("Lesson deleted.");
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/hard/{id}")
    public void hardDeleteLesson(@PathVariable Long id){
        lessonService.hardDeleteLesson(id);
        ResponseEntity.status(HttpStatus.OK).body("Lesson deleted.");
    }
}
