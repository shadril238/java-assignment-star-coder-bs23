package com.shadril.taskmanagementjava.dto;

import com.shadril.taskmanagementjava.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserLoginResponseDto {
    private String message;
    private HttpStatus status;
    private Long userId;
    private String email;
    private String username;
    private Role role;
    private String token;
}
