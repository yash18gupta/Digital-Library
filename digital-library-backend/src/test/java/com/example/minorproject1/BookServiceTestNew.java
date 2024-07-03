//package com.example.minorproject1;
//
//import com.example.minorproject1.dto.CreateBookRequest;
//import com.example.minorproject1.model.Author;
//import com.example.minorproject1.model.Book;
//import com.example.minorproject1.model.Student;
//import com.example.minorproject1.model.enums.Genre;
//import com.example.minorproject1.repository.BookRepository;
//import com.example.minorproject1.service.AuthorService;
//import com.example.minorproject1.service.BookService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//public class BookServiceTestNew {
//
//    @InjectMocks
//    BookService bookService;
//
//    @Mock
//    BookRepository bookRepository;
//
//    @Mock
//    AuthorService authorService;
//
//    private Book book;
//    private Author author;
//    private CreateBookRequest createBookRequest;
//
//    private Student student;
//
//    @BeforeEach
//    void setUp() {
//        author = Author.builder()
//                .id(1)
//                .email("auhtor@example.com")
//                .name("author")
//                .country("country")
//                .build();
//
//        book = Book.builder()
//                .id(1)
//                .name("Life of Pi")
//                .genre(Genre.FICTION)
//                .pages(100)
//                .author(author)
//                .build();
//
//        createBookRequest = CreateBookRequest.builder()
//                .name("Life of Pi")
//                .genre(Genre.FICTION)
//                .pages(100)
//                .authorName("author")
//                .authorEmail("auhtor@example.com")
//                .authorCountry("country")
//                .build();
//
//        student = Student.builder()
//                .id(1)
//                .name("Yash")
//                .build();
//    }
//
//    @Test
//    public void createBookTest(){
//        when(authorService.createOrGet(any(Author.class))).thenReturn(author);
//        when(bookRepository.save(any(Book.class))).thenReturn(book);
//
//        Book result = bookService.createBook(createBookRequest);
//
//        assertNotNull(result);
//        assertEquals("Life of Pi", result.getName());
//
//        verify(authorService,times(1)).createOrGet(any(Author.class));
//        verify(bookRepository,times(1)).save(any(Book.class));
//    }
//
//    @Test
//    public void getAllTest(){
//        when(bookRepository.getAvailableBook()).thenReturn(List.of(book));
//
//        List<Book> result = bookService.getAll();
//
//        assertNotNull(result);
//        assertEquals(1,result.size());
//        assertEquals("Life of Pi",result.get(0).getName());
//
//        verify(bookRepository,times(1)).getAvailableBook();
//    }
//
//    @Test
//    public void deleteBookByIdSuccessTest() throws Exception {
//        when(bookRepository.findById(1)).thenReturn(Optional.ofNullable(book));
//
//        Book result = bookService.deleteBookById(1);
//
//        assertNotNull(result);
//        assertEquals(1,result.getId());
//
//        verify(bookRepository,times(1)).findById(1);
//        verify(bookRepository,times(1)).deleteById(1);
//
//    }
//
//    @Test
//    void testDeleteBookByIdNotFound(){
//        when(bookRepository.findById(1)).thenReturn(Optional.empty());
//
//        Exception exception = assertThrows(Exception.class, () -> {
//            bookService.deleteBookById(1);
//        });
//
//        assertEquals("Book not found!", exception.getMessage());
//        verify(bookRepository, times(1)).findById(1);
//        verify(bookRepository, times(0)).deleteById(1);
//    }
//
//    @Test
//    void testDeleteBookByIdAllocatedToStudent(){
//        book.setStudent(student);
//        when(bookRepository.findById(1)).thenReturn(Optional.ofNullable(book));
//
//        Exception exception = assertThrows(Exception.class,()->{
//            bookService.deleteBookById(1);
//        });
//
//        assertEquals("Book can't be deleted - Allocated to student", exception.getMessage());
//        verify(bookRepository, times(1)).findById(1);
//        verify(bookRepository, times(0)).deleteById(1);
//    }
//
//    @Test
//    void testGetAllAvailable(){
//        when(bookRepository.getAllAvailable("Life of Pi","author",Genre.FICTION,50,150)).thenReturn(List.of(book));
//
//        List<Book> result = bookService.getAllAvailable(
//                Optional.of("Life of Pi"),
//                Optional.of("author"),
//                Optional.of(Genre.FICTION),
//                Optional.of(50),
//                Optional.of(150)
//        );
//
//        assertNotNull(result);
//        assertEquals(1,result.size());
//        verify(bookRepository,times(1)).getAllAvailable("Life of Pi","author",Genre.FICTION,50,150);
//    }
//
//    @Test
//    void testGetAllAvailableWithNoParameters() {
//        when(bookRepository.getAllAvailable(null, null, null, 0, Integer.MAX_VALUE))
//                .thenReturn(List.of(book));
//
//        List<Book> books = bookService.getAllAvailable(
//                Optional.empty(),
//                Optional.empty(),
//                Optional.empty(),
//                Optional.empty(),
//                Optional.empty()
//        );
//
//        assertNotNull(books);
//        assertEquals(1, books.size());
//        verify(bookRepository, times(1)).getAllAvailable(null, null, null, 0, Integer.MAX_VALUE);
//    }
//
//    @Test
//    void testAssignBooks(){
//        bookService.assignBookToStudent(book,student);
//
//        verify(bookRepository,times(1)).assignBookToStudent(1,student);
//    }
//
//
//    @Test
//    void testUnassignBooks(){
//        bookService.unassignBookFromStudent(book);
//
//        verify(bookRepository,times(1)).unassignBook(1);
//    }
//
//    @Test
//    void testGetBookById(){
//        when(bookRepository.findById(1)).thenReturn(Optional.ofNullable(book));
//
//        Book result = bookService.getBookById(1);
//
//        assertNotNull(result);
//        assertEquals(1,result.getId());
//
//        verify(bookRepository,times(1)).findById(1);
//    }
//
//    @Test
//    void testGetBookByIdNotFound() {
//        when(bookRepository.findById(1)).thenReturn(Optional.empty());
//
//        Book foundBook = bookService.getBookById(1);
//
//        assertNull(foundBook);
//        verify(bookRepository, times(1)).findById(1);
//    }
//
//}
