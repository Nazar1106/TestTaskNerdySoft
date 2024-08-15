package com.example.testtasknerdysoft.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberDTO {

    private Long memberId;

    @NotBlank(message = "Name is required")
    private String name;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate membershipDate = LocalDate.now();

    private Set<BookDTO> borrowedBooks;
}
