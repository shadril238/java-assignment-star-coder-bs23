package com.shadril.taskmanagementjava.dto;

import lombok.*;
import org.springframework.http.HttpStatus;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResponseMessageDto {
    private String message;
    private HttpStatus status;
}
