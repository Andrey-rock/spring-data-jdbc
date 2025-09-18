package com.example.spring_data_jdbc.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("books")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Book {
    @Id
    private Long id;
    private String title;
    private String author;
    private Integer publicationYear;
}
