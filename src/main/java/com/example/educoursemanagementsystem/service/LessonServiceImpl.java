package com.example.educoursemanagementsystem.service;


import com.example.educoursemanagementsystem.dto.request.CourseRequestDTO;
import com.example.educoursemanagementsystem.dto.request.LessonRequest;
import com.example.educoursemanagementsystem.dto.response.LessonResponse;
import com.example.educoursemanagementsystem.entity.Course;
import com.example.educoursemanagementsystem.entity.Lesson;
import com.example.educoursemanagementsystem.mapper.LessonMapper;
import com.example.educoursemanagementsystem.repository.CourseRepository;
import com.example.educoursemanagementsystem.repository.LessonRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class LessonServiceImpl implements LessonService {

    private final LessonRepository lessonRepository;
    private final LessonMapper lessonMapper;
    private  final CourseRepository courseRepository;

    @Override
    public LessonResponse createLesson(LessonRequest request) {
        if (request.getCourseId() == null) {
            throw new RuntimeException("CourseId boş ola bilməz");
        }

        Course course = courseRepository.findById(request.getCourseId())
                .orElseThrow(() -> new RuntimeException("Kurs tapılmadı: " + request.getCourseId()));

        Lesson lesson = Lesson.builder()
                .title(request.getTitle())
                .videoURL(request.getVideoURL())
                .content(request.getContent())
                .course(course)
                .build();

        Lesson saved = lessonRepository.save(lesson);
        return lessonMapper.toLessonResponse(saved);
    }

    @Override
    public LessonResponse getLessonById(Long id) {
        Lesson lesson=lessonRepository.findById(id).orElseThrow(()->new RuntimeException("Lesson not found"));
        return lessonMapper.toLessonResponse(lesson);
    }

    @Override
    public List<LessonResponse> getAllLessons() {
        return lessonRepository.findAll().stream()
                .map(lessonMapper :: toLessonResponse)
                .toList();

    }

    @Override
    public List<LessonResponse> getAllActiveLessons() {
        return lessonRepository.findByIsActive(true).stream()
                .map(lessonMapper :: toLessonResponse)
                .toList();
    }


    //GlobalException yaradan zaman buraya fikir ver.
    @Override
    public List<LessonResponse> searchLessonsByTitle(String title) {
        return lessonRepository.findByTitleIgnoreCase(title).stream()
                .map(lessonMapper :: toLessonResponse)
                .toList();

    }

    @Override
    public LessonResponse updateLesson(Long id, LessonRequest request) {
        Lesson lesson=lessonRepository.findById(id).orElseThrow(()->new RuntimeException("Lesson not found"));
        lesson.setTitle(request.getTitle());
        lesson.setVideoURL(request.getVideoURL());
        lesson.setContent(request.getContent());

        Lesson updated=lessonRepository.save(lesson);
        return lessonMapper.toLessonResponse(updated);
    }

    @Override
    public LessonResponse updateVideoUrl(Long lessonId, String videoUrl) {
        Lesson lesson=lessonRepository.findById(lessonId).orElseThrow(()->new RuntimeException("Lesson not found"));
        lesson.setVideoURL(videoUrl);
        Lesson updated= lessonRepository.save(lesson);
        return lessonMapper.toLessonResponse(updated);

    }

    @Override
    public void softDeleteLesson(Long id) {
        Lesson lesson=lessonRepository.findById(id).orElseThrow(()->new RuntimeException("Lesson not found"));
        lesson.setIsActive(false);
        lessonRepository.save(lesson);
    }

    @Override
    public void hardDeleteLesson(Long id) {
        Lesson lesson=lessonRepository.deleteLessonById(id).orElseThrow(()->new RuntimeException("Lesson not found"));


    }
}
