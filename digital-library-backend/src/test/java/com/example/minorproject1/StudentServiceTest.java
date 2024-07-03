package com.example.minorproject1;

import com.example.minorproject1.dto.CreateStudentRequest;
import com.example.minorproject1.model.Book;
import com.example.minorproject1.model.SecuredUser;
import com.example.minorproject1.model.Student;
import com.example.minorproject1.model.enums.Genre;
import com.example.minorproject1.repository.StudentCacheRepository;
import com.example.minorproject1.repository.StudentRepository;
import com.example.minorproject1.service.SecuredUserService;
import com.example.minorproject1.service.StudentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class StudentServiceTest {

    @InjectMocks
    StudentService studentService;

    @Mock
    StudentRepository studentRepository;

    @Mock
    SecuredUserService securedUserService;

    @Mock
    StudentCacheRepository studentCacheRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Value("${student.authorities}")
    private String authorities;

    private Student student;
    private SecuredUser securedUser;

    @BeforeEach
    void setUp() {
        securedUser = SecuredUser.builder()
                .id(1)
                .username("username")
                .password("encodedPassword")
                .build();

        student = Student.builder()
                .id(1)
                .name("name")
                .contact("contact")
                .securedUser(securedUser)
                .build();


    }

    @Test
    void testCreateStudent() {
        CreateStudentRequest createStudentRequest = CreateStudentRequest.builder()
                .name("name")
                .contact("contact")
                .username("username")
                .password("password")
                .build();

        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");
        when(securedUserService.save(any(SecuredUser.class))).thenReturn(securedUser);
        when(studentRepository.save(any(Student.class))).thenReturn(student);


        Student result = studentService.create(createStudentRequest);

        assertNotNull(result);
        assertEquals(1,result.getId());
        assertEquals("encodedPassword",result.getSecuredUser().getPassword());

        verify(passwordEncoder,times(1)).encode("password");
        verify(studentRepository,times(1)).save(any(Student.class));
        verify(securedUserService,times(1)).save(any(SecuredUser.class));

    }

    @Test
    void testGetStudentSuccess(){
        when(studentRepository.findById(1)).thenReturn(Optional.of(student));

        Student result = studentService.getStudent(1);

        assertNotNull(result);
        assertEquals("name",result.getName());
        verify(studentRepository,times(1)).findById(1);
    }

    @Test
    void testGetStudentNotExists(){
        when(studentRepository.findById(1)).thenReturn(Optional.empty());

        Student result = studentService.getStudent(1);

        assertNull(result);
        verify(studentRepository,times(1)).findById(1);
    }



}
