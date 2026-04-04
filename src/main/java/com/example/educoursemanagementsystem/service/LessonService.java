package com.example.educoursemanagementsystem.service;

import com.example.educoursemanagementsystem.model.dto.request.LessonRequest;
import com.example.educoursemanagementsystem.model.dto.response.LessonResponse;

import java.util.List;

public interface LessonService {
    LessonResponse createLesson(LessonRequest request);

    LessonResponse getLessonById(Long id);

    List<LessonResponse> getAllLessons();

    List<LessonResponse> getAllActiveLessons();

    List<LessonResponse> searchLessonsByTitle(String title);

    LessonResponse updateLesson(Long id, LessonRequest request);

    LessonResponse updateVideoUrl(Long lessonId, String videoUrl);

    void softDeleteLesson(Long id);

    void hardDeleteLesson(Long id);

    List<LessonResponse> getLessonsByCourseForStudent(Long courseId);
}
