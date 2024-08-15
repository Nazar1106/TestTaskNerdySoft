package com.example.testtasknerdysoft.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class BorrowedBookDTO {

    private String name;
    private Integer borrowedQuantity;
}
