package com.example.minorproject1.controller;

import com.example.minorproject1.dto.CreateStudentRequest;
import com.example.minorproject1.dto.StudentResponse;
import com.example.minorproject1.dto.UpdateStudentResponse;
import com.example.minorproject1.model.Book;
import com.example.minorproject1.model.SecuredUser;
import com.example.minorproject1.model.Student;
import com.example.minorproject1.service.StudentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/student")
public class StudentController {

    @Autowired
    StudentService studentService;

    @PostMapping("/create")
    public Student createStudent(@RequestBody CreateStudentRequest createStudentRequest){
        return studentService.create(createStudentRequest);
    }

    @GetMapping("/get")
    @PreAuthorize("hasAuthority('student')")
    public Student getStudentforAdmin(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        SecuredUser securedUser = (SecuredUser) authentication.getPrincipal();
        int studentId = securedUser.getStudent().getId();
        return studentService.getStudent(studentId);
    }

    @GetMapping("/details")
    @PreAuthorize("hasAuthority('student')")
    public StudentResponse getStudent(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        SecuredUser securedUser = (SecuredUser) authentication.getPrincipal();
        int studentId = securedUser.getStudent().getId();

        return studentService.getStudentFromCache(studentId);
    }

    @DeleteMapping("/delete")
    @PreAuthorize("hasAuthority('student')")
    public Student deleteStudentById() throws Exception {

        System.out.println("Deleting!!!");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        SecuredUser securedUser = (SecuredUser) authentication.getPrincipal();
        int studentId = securedUser.getStudent().getId();
        return studentService.deleteStudentById(studentId);
    }

    @GetMapping("/getAll")
    @PreAuthorize("hasAuthority('admin')")
    public List<Student> getAllStudents(){
        return studentService.getAll();
    }

    @PutMapping("/update")
    @PreAuthorize("hasAuthority('student')")
    public Student updateStudent(@RequestBody @Valid UpdateStudentResponse updateStudentResponse) throws Exception {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        SecuredUser securedUser = (SecuredUser) authentication.getPrincipal();
        int studentId = securedUser.getStudent().getId();
        return studentService.updateStudent(studentId,updateStudentResponse);
    }

}
