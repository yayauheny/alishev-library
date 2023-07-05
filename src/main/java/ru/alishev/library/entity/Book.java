package ru.alishev.library.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Year;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Book {

    private Integer id;
    private String title;
    private String author;
    private Year year;
}
