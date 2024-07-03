package com.example.minorproject1.dto;


import com.example.minorproject1.model.Student;
import jakarta.persistence.Entity;
import lombok.*;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentResponse implements Serializable {
    private int id;

    private String name;
    private String contact;

    private String username;

    //private String authorities;

    private Date createdOn;

    private Date updatedOn;

    private Date validity;

    public StudentResponse(Student student){
        this.id = student.getId();
        this.name = student.getName();
        this.contact = student.getContact();
        this.username = student.getSecuredUser().getUsername();
        this.createdOn = student.getCreatedOn();
        this.updatedOn = student.getUpdatedOn();
    }

}
