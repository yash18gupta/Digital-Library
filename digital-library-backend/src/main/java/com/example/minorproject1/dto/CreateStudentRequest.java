package com.example.minorproject1.dto;

import com.example.minorproject1.model.SecuredUser;
import com.example.minorproject1.model.Student;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateStudentRequest {

    @NotBlank
    private String name;
    @NotBlank
    private String contact;
    @NotBlank
    private String username;
    @NotBlank
    private String password;

    public Student to(){
        return Student.builder()
                .name(this.name)
                .contact(this.contact)
                .securedUser(SecuredUser.builder()
                        .username(this.username)
                        .password(this.password)
                        .build())
                .build();
    }
}
