package com.example.minorproject1.service;

import com.example.minorproject1.dto.BookFrontend;
import com.example.minorproject1.dto.CreateBookRequest;
import com.example.minorproject1.model.Author;
import com.example.minorproject1.model.Book;
import com.example.minorproject1.model.FileData;
import com.example.minorproject1.model.Student;
import com.example.minorproject1.model.enums.Genre;
import com.example.minorproject1.repository.BookRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Service
public class BookService {

    @Autowired
    BookRepository bookRepository;

    @Autowired
    AuthorService authorService;

    @Autowired
    FileService fileService;

    public Book createBook(CreateBookRequest createBookResponse) throws Exception {

        Book book = createBookResponse.to();

        Author author = authorService.createOrGet(book.getAuthor());
        book.setAuthor(author);

        FileData fileData = fileService.saveAttachment(createBookResponse.getFile());
        book.setFileData(fileData);

        return bookRepository.save(book);
    }

    public List<Book> getAll() {
        return bookRepository.getAvailableBook();
    }

    @Transactional
    public Book deleteBookById(int bookId) throws Exception {
        Book book;

        try {
            book = bookRepository.findById(bookId).orElseThrow(NoSuchElementException::new);
        } catch (NoSuchElementException e) {
            e.printStackTrace();
            throw new Exception("Book not found!");
        }

        if (book.getStudent() == null) {
            bookRepository.deleteById(bookId);
            return book;
        } else {
            throw new Exception("Book can't be deleted - Allocated to student");
        }
    }

    public List<Book> getAllAvailable(Optional<String> name, Optional<String> author, Optional<Genre> genre, Optional<Integer> minPages, Optional<Integer> maxPages) {
        return bookRepository.getAllAvailable(
                name.orElse(null),
                author.orElse(null),
                genre.orElse(null),
                minPages.orElse(0),
                maxPages.orElse(Integer.MAX_VALUE)
        );
    }

    public void assignBookToStudent(Book book, Student student) {
        bookRepository.assignBookToStudent(book.getId(), student);
    }

    public void unassignBookFromStudent(Book book) {
        bookRepository.unassignBook(book.getId());
    }

    public Book getBookById(int id) {
        return bookRepository.findById(id).orElse(null);
    }

    public List<BookFrontend> showBooks() {
        List<Book> availableBooks = bookRepository.getAvailableBook();

        Map<Book, Integer> bookCountMap = new HashMap<>();
        for (Book b : availableBooks) {
            bookCountMap.put(b, bookCountMap.getOrDefault(b, 0) + 1);
        }

        List<BookFrontend> response = new ArrayList<>();

        for (Map.Entry<Book, Integer> b : bookCountMap.entrySet()) {
            Book book = b.getKey();
            Integer count = b.getValue();

            BookFrontend bookFrontend = BookFrontend.builder()
                    .id(book.getId())
                    .name(book.getName())
                    .genre(book.getGenre())
                    .pages(book.getPages())
                    .authorName(book.getAuthor().getName())
                    .count(count)
                    .imageUrl(book.getImageUrl())
                    .build();

            response.add(bookFrontend);
        }

        return response;

    }
}
