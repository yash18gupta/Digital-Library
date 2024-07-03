package com.example.minorproject1.controller;

import com.example.minorproject1.model.Author;
import com.example.minorproject1.service.AuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/author")
public class AuthorController {

    @Autowired
    AuthorService authorService;

    @GetMapping("/getAll")
    @PreAuthorize("hasAnyAuthority('student','admin')")
    public List<Author> getAllAuthor(){
        return authorService.getAllAuthor();
    }
}
