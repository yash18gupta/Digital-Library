package com.example.minorproject1.controller;

import com.example.minorproject1.dto.BookFrontend;
import com.example.minorproject1.dto.CreateBookRequest;
import com.example.minorproject1.dto.ResponseData;
import com.example.minorproject1.model.Book;
import com.example.minorproject1.model.FileData;
import com.example.minorproject1.model.enums.Genre;
import com.example.minorproject1.service.BookService;
import com.example.minorproject1.service.FileService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/book")
public class BookController {

    @Autowired
    BookService bookService;

    @Autowired
    FileService fileService;

    @PostMapping("/create")
    @PreAuthorize("hasAuthority('admin')")
    public ResponseData createBook(@ModelAttribute  CreateBookRequest createBookResponse) throws Exception {
        FileData fileData = null;
        String downloadURl = "";
        fileData = bookService.createBook(createBookResponse).getFileData();
        downloadURl = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/download/")
                .path(fileData.getId())
                .toUriString();

        return new ResponseData(fileData.getName(),
                downloadURl,
                createBookResponse.getFile().getContentType(),
                createBookResponse.getFile().getSize());
    }

    @GetMapping("/getAll")
    @PreAuthorize("hasAnyAuthority('student','admin')")
    public List<Book> getAllBooks(){
        return bookService.getAll();
    }

    @GetMapping("/get/{id}")
    @PreAuthorize("hasAnyAuthority('student', 'admin')")
    public Book getBookById(@PathVariable int id){
        return bookService.getBookById(id);
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('admin')")
    public Book deleteBookById(@PathVariable int id) throws Exception {
        return bookService.deleteBookById(id);
    }
    @GetMapping("/download/{fileId}")
    @PreAuthorize("hasAnyAuthority('student', 'admin')")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileId) throws Exception {
        FileData fileData = null;
        fileData = fileService.getAttachment(fileId);
        return  ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(fileData.getType()))
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "fileData; filename=\"" + fileData.getName()
                                + "\"")
                .body(new ByteArrayResource(fileData.getFileData()));
    }

    @GetMapping("/search")
    @PreAuthorize("hasAnyAuthority('student','admin')")
    public List<Book> getAllAvailable(@RequestParam(value = "name" ,required = false) String name,
                                      @RequestParam(value= "author" ,required = false) String author,
                                      @RequestParam(value = "genre" ,required = false) Genre genre,
                                      @RequestParam(value = "minPages" ,required = false) Integer minPages,
                                      @RequestParam(value = "maxPages" ,required = false) Integer maxPages
                                      ){
        return bookService.getAllAvailable(Optional.ofNullable(name), Optional.ofNullable(author), Optional.ofNullable(genre), Optional.ofNullable(minPages), Optional.ofNullable(maxPages));
    }


    @GetMapping("/frontend")
    @PreAuthorize("hasAuthority('student')")
    public List<BookFrontend> showBooks(){
        return bookService.showBooks();
    }

}
