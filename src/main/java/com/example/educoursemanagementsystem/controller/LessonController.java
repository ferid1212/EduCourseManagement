package com.example.educoursemanagementsystem.controller;


import com.example.educoursemanagementsystem.dto.request.CourseRequestDTO;
import com.example.educoursemanagementsystem.dto.request.LessonRequest;
import com.example.educoursemanagementsystem.dto.response.LessonResponse;
import com.example.educoursemanagementsystem.service.LessonService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/lessons")
public class LessonController {

    private final LessonService lessonService;

    @PostMapping()
    public ResponseEntity<LessonResponse> createLesson( @Valid @RequestBody LessonRequest lessonRequest){
        LessonResponse response=lessonService.createLesson(lessonRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LessonResponse> getLessonById(@PathVariable Long id){
        LessonResponse response=lessonService.getLessonById(id);
        return ResponseEntity.status(HttpStatus.OK).body(response);

    }

    @GetMapping
    public ResponseEntity<List<LessonResponse>> getAllLessons(){
        List<LessonResponse> responses=lessonService.getAllLessons();
        return ResponseEntity.status(HttpStatus.OK).body(responses);
    }

    @GetMapping("/active")
    public ResponseEntity<List<LessonResponse>> getAllActiveLessons(){
        List<LessonResponse> responses=lessonService.getAllActiveLessons();
        return ResponseEntity.status(HttpStatus.OK).body(responses);

    }

    @GetMapping("/title/{title}")
    public ResponseEntity<List<LessonResponse>> searchLessonByTitle(@PathVariable String title){
        List<LessonResponse> responses=lessonService.searchLessonsByTitle(title);
        return ResponseEntity.status(HttpStatus.OK).body(responses);
    }

    @PutMapping("/{id}")
    public ResponseEntity<LessonResponse> updateLesson(@PathVariable Long id,@Valid @RequestBody LessonRequest request){
        LessonResponse response=lessonService.updateLesson(id,request);
        return ResponseEntity.status(HttpStatus.OK).body(response);

    }

    @PutMapping("/video/{id}/{videoURL}")
    public ResponseEntity<LessonResponse> updateVideoURL(@PathVariable Long id,
                                                         @PathVariable String videoURL){
        LessonResponse response=lessonService.updateVideoUrl(id,videoURL);
        return ResponseEntity.status(HttpStatus.OK).body(response);

    }

    @DeleteMapping("/{id}")
    public void softDeleteLesson(@PathVariable Long id){
        lessonService.softDeleteLesson(id);
        ResponseEntity.status(HttpStatus.OK).body("Lesson deleted.");
    }

    @DeleteMapping("/hard/{id}")
    public void hardDeleteLesson(@PathVariable Long id){
        lessonService.hardDeleteLesson(id);
        ResponseEntity.status(HttpStatus.OK).body("Lesson deleted.");
    }
}
