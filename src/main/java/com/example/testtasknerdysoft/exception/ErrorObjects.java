package com.example.testtasknerdysoft.exception;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ErrorObjects {

    private long httpStatus;
    private String detail;
    private LocalDate time;
}
