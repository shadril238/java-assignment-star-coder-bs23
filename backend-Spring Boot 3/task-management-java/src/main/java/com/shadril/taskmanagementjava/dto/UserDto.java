package com.shadril.taskmanagementjava.dto;

import com.shadril.taskmanagementjava.enums.Role;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {
    private Long id;

    @NotBlank(message = "Username is required")
    private String username;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@#$%^&+=]).*$",
            message = "Password must contain at least one uppercase letter, one lowercase letter, one digit, and one special character")
    private String password;

    @NotBlank(message = "First name is required")
    @Size(max = 15, message = "First name can't exceed 15 characters")
    private String firstname;

    @NotBlank(message = "Last name is required")
    @Size(max = 15, message = "Last name can't exceed 15 characters")
    private String lastname;

    @NotNull(message = "Role is required")
    private Role role;

    private Boolean isDeleted = false;

    private List<TaskDto> tasks;
}
