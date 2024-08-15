package com.example.testtasknerdysoft.dto;

import com.example.testtasknerdysoft.validation.CapitalLetter;
import com.example.testtasknerdysoft.validation.FullName;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class BookDTO {

    private Long id;
    @NotBlank(message = "Title is required")
    @Size(min = 3, message = "Title must be at least 3 characters long")
    @CapitalLetter
    private String title;
    @NotBlank(message = "Author is required")
    @FullName
    private String author;
    private int amount;
}
