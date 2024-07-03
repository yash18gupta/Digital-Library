package com.example.minorproject1.controller;

import com.example.minorproject1.dto.CreateAdminRequest;
import com.example.minorproject1.dto.CreateStudentRequest;
import com.example.minorproject1.model.Admin;
import com.example.minorproject1.model.Student;
import com.example.minorproject1.service.AdminService;
import com.example.minorproject1.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    AdminService adminService;

    @PostMapping("/create")
    @PreAuthorize("hasAuthority('admin')")
    public Admin createAdmin(@RequestBody CreateAdminRequest createAdminRequest){
        return adminService.create(createAdminRequest);
    }
}
