package com.example.testtasknerdysoft.advice;

import com.example.testtasknerdysoft.exception.ApiRequestException;
import com.example.testtasknerdysoft.exception.BusinessException;
import com.example.testtasknerdysoft.exception.ErrorObjects;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDate;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = {ApiRequestException.class})
    public ResponseEntity<ErrorObjects> handleApiRequestException(ApiRequestException e) {
        ErrorObjects errorObjects = new ErrorObjects();
        errorObjects.setHttpStatus(HttpStatus.BAD_REQUEST.value());
        errorObjects.setDetail(e.getMessage());
        errorObjects.setTime(LocalDate.now());
        return new ResponseEntity<>(errorObjects, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {BusinessException.class})
    public ResponseEntity<ErrorObjects> handleBusinessException(BusinessException e) {
        ErrorObjects errorObjects = new ErrorObjects();
        errorObjects.setHttpStatus(HttpStatus.NOT_FOUND.value());
        errorObjects.setDetail(e.getMessage());
        errorObjects.setTime(LocalDate.now());
        return new ResponseEntity<>(errorObjects, HttpStatus.NOT_FOUND);
    }
}
