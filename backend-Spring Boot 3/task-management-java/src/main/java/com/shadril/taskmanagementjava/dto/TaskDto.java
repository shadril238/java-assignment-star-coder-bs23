package com.shadril.taskmanagementjava.dto;

import com.shadril.taskmanagementjava.enums.TaskStatus;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskDto {
    private Long id;

    @NotBlank(message = "Title is required")
    @Size(max = 50, message = "Title can't exceed 50 characters")
    private String title;

    @Size(max = 250, message = "Description can't exceed 250 characters")
    private String description;

    @NotNull(message = "Status is required")
    private TaskStatus status;

    @NotNull
    private LocalDateTime createdAt = LocalDateTime.now();

    private LocalDateTime updatedAt;

    private Boolean isDeleted = false;

    private Long userId;
}
