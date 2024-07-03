package com.example.minorproject1.dto;

import com.example.minorproject1.model.enums.Genre;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookFrontend {

    private int id;

    private String name;

    private Genre genre;

    private String authorName;

    private Integer pages;

    private Integer count;

    private String imageUrl;

}
