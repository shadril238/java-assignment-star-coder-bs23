package com.shadril.taskmanagementjava.controller;

import com.shadril.taskmanagementjava.dto.ResponseMessageDto;
import com.shadril.taskmanagementjava.exception.CustomException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ResponseMessageDto> handleCustomException(CustomException e) {
        log.error("Exception occurred: ", e);
        ResponseMessageDto errorResponse = new ResponseMessageDto(e.getMessage(), e.getStatus());
        return new ResponseEntity<>(errorResponse, e.getStatus());
    }
}
