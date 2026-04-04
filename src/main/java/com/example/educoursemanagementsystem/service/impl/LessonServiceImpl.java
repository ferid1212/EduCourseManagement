package com.example.educoursemanagementsystem.service.impl;
import com.example.educoursemanagementsystem.enums.EnrollmentStatus;
import com.example.educoursemanagementsystem.model.entity.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.Authentication;
import com.example.educoursemanagementsystem.repository.TeacherRepository;
import com.example.educoursemanagementsystem.repository.StudentRepository;
import com.example.educoursemanagementsystem.repository.EnrollmentRepository;


import com.example.educoursemanagementsystem.exception.ResourceNotFoundException;
import com.example.educoursemanagementsystem.model.dto.request.LessonRequest;
import com.example.educoursemanagementsystem.model.dto.response.LessonResponse;
import com.example.educoursemanagementsystem.mapper.LessonMapper;
import com.example.educoursemanagementsystem.repository.CourseRepository;
import com.example.educoursemanagementsystem.repository.LessonRepository;
import com.example.educoursemanagementsystem.service.LessonService;
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
    private final CourseRepository courseRepository;
    private final TeacherRepository teacherRepository;
    private final StudentRepository studentRepository;
    private final EnrollmentRepository enrollmentRepository;

    @Override
    public LessonResponse createLesson(LessonRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        boolean isTeacher = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_TEACHER"));

        Course course;
        if (isTeacher) {
            Teacher teacher = teacherRepository.getTeachersByEmail(email)
                    .orElseThrow(() -> new ResourceNotFoundException("Müəllim tapılmadı: " + email));
            course = teacher.getCourse();
            if (course == null) {
                throw new RuntimeException("Müəllim hələ bir kursa təyin edilməyib.");
            }
        } else {
            if (request.getCourseId() == null) {
                throw new RuntimeException("CourseId boş ola bilməz (Admin üçün məcburidir).");
            }
            course = courseRepository.findById(request.getCourseId())
                    .orElseThrow(() -> new ResourceNotFoundException("Kurs tapılmadı: " + request.getCourseId()));
        }

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
        Lesson lesson=lessonRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Lesson not found"));
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


    @Override
    public List<LessonResponse> searchLessonsByTitle(String title) {
        return lessonRepository.findByTitleContainingIgnoreCase(title).stream()
                .map(lessonMapper::toLessonResponse)
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
        Lesson lesson=lessonRepository.findById(lessonId).orElseThrow(()->new ResourceNotFoundException("Lesson not found"));
        lesson.setVideoURL(videoUrl);
        Lesson updated= lessonRepository.save(lesson);
        return lessonMapper.toLessonResponse(updated);

    }

    @Override
    public void softDeleteLesson(Long id) {
        Lesson lesson=lessonRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Lesson not found"));
        lesson.setIsActive(false);
        lessonRepository.save(lesson);
    }

    @Override
    public void hardDeleteLesson(Long id) {
        Lesson lesson=lessonRepository.deleteLessonById(id).orElseThrow(()->new ResourceNotFoundException("Lesson not found"));


    }
    @Override
    public List<LessonResponse> getLessonsByCourseForStudent(Long courseId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();

        Student student = studentRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found: " + email));

        if (!student.getIsActive()) {
            throw new RuntimeException("Student account is not active.");
        }

        Enrollment enrollment = enrollmentRepository.findByStudentIdAndCourseId(student.getId(), courseId)
                .orElseThrow(() -> new RuntimeException("You are not enrolled in this course."));

        if (!Boolean.TRUE.equals(enrollment.getIsActive()) || enrollment.getStatus() != EnrollmentStatus.ACTIVE) {
            throw new RuntimeException("Your enrollment is not active or awaiting payment.");
        }

        return lessonRepository.findByCourseIdAndIsActiveTrue(courseId).stream()
                .map(lessonMapper::toLessonResponse)
                .toList();
    }
}
