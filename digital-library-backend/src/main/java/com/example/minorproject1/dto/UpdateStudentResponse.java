package com.example.minorproject1.dto;

import com.example.minorproject1.model.Student;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateStudentResponse {
    @NotBlank
    private String name;

    @NotBlank
    private String contact;

    private Date validity;


    public Student to(){
        return Student.builder()
                .name(this.name)
                .contact(this.contact)
                .build();
    }
}
