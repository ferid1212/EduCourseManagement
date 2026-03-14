package com.example.educoursemanagementsystem.service;

import com.example.educoursemanagementsystem.dto.request.TeacherRequest;
import com.example.educoursemanagementsystem.dto.response.TeacherResponse;

import java.util.List;

public interface TeacherService {

    TeacherResponse createTeacher(TeacherRequest request);
    TeacherResponse getTeacherById(Long id);
    List<TeacherResponse> getAllTeachers();
    List<TeacherResponse> getAllActiveTeachers();
    void updateTeacher(Long id, TeacherRequest request);
    void softDeleteTeacher(Long id);
    void hardDeleteTeacher(Long id);
    TeacherResponse getTeacherByEmail(String email);
    List<TeacherResponse> searchTeachersByName(String name);
}
