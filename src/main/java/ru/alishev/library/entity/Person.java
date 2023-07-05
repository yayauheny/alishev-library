package ru.alishev.library.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Person {

    private Integer id;
    private String fullName;
    private LocalDate birthdayDate;
}

