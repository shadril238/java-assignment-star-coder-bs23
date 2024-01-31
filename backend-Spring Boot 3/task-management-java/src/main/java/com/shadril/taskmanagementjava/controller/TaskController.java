package com.shadril.taskmanagementjava.controller;

import com.shadril.taskmanagementjava.dto.ResponseMessageDto;
import com.shadril.taskmanagementjava.dto.TaskDto;
import com.shadril.taskmanagementjava.exception.CustomException;
import com.shadril.taskmanagementjava.service.TaskService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/api/tasks")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @PostMapping("/")
    public ResponseEntity<?> createTask(@Valid @RequestBody TaskDto taskDto) {
        try {
            log.info("Creating task for user ID: {}", taskDto.getUserId());
            TaskDto createdTask = taskService.createTask(taskDto);
            return new ResponseEntity<>(createdTask, HttpStatus.CREATED);
        } catch (CustomException e) {
            log.error("Error creating task: {}", e.getMessage());
            return new ResponseEntity<>(new ResponseMessageDto(e.getMessage(), e.getStatus()), e.getStatus());
        }
    }

    @GetMapping("/")
    public ResponseEntity<?> getAllTasks() {
        try {
            log.info("Fetching all tasks");
            List<TaskDto> tasks = taskService.getAllTasks();
            return ResponseEntity.ok(tasks);
        } catch (CustomException e) {
            log.error("Error fetching all tasks", e);
            return new ResponseEntity<>(new ResponseMessageDto(e.getMessage(), e.getStatus()), e.getStatus());
        }
    }

    @PutMapping("/{taskId}")
    public ResponseEntity<?> updateTask(@PathVariable Long taskId, @Valid @RequestBody TaskDto taskDto) {
        try {
            log.info("Updating task ID: {}", taskId);
            TaskDto updatedTask = taskService.updateTask(taskId, taskDto);
            return new ResponseEntity<>(updatedTask, HttpStatus.OK);
        } catch (CustomException e) {
            log.error("Error updating task ID: {}", taskId, e);
            return new ResponseEntity<>(new ResponseMessageDto(e.getMessage(), e.getStatus()), e.getStatus());
        }
    }

    @DeleteMapping("/{taskId}")
    public ResponseEntity<?> deleteTask(@PathVariable Long taskId) {
        try {
            log.info("Deleting task ID: {}", taskId);
            taskService.deleteTask(taskId);
            return new ResponseEntity<>(new ResponseMessageDto("Task deleted successfully", HttpStatus.OK), HttpStatus.OK);
        } catch (CustomException e) {
            log.error("Error deleting task ID: {}", taskId, e);
            return new ResponseEntity<>(new ResponseMessageDto(e.getMessage(), e.getStatus()), e.getStatus());
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getAllTasksByUserId(@PathVariable Long userId) {
        try {
            log.info("Fetching tasks for user ID: {}", userId);
            List<TaskDto> tasks = taskService.getAllTasksByUserId(userId);
            return ResponseEntity.ok(tasks);
        } catch (CustomException e) {
            log.error("Error fetching tasks for user ID: {}", userId, e);
            return new ResponseEntity<>(new ResponseMessageDto(e.getMessage(), e.getStatus()), e.getStatus());
        }
    }

    @GetMapping("/{taskId}")
    public ResponseEntity<?> getTaskById(@PathVariable Long taskId) {
        try {
            log.info("Fetching task ID: {}", taskId);
            TaskDto task = taskService.getTaskById(taskId);
            return ResponseEntity.ok(task);
        } catch (CustomException e) {
            log.error("Error fetching task ID: {}", taskId, e);
            return new ResponseEntity<>(new ResponseMessageDto(e.getMessage(), e.getStatus()), e.getStatus());
        }
    }

    @PostMapping("/{taskId}/complete")
    public ResponseEntity<?> completeTask(@PathVariable Long taskId) {
        try {
            log.info("Completing task ID: {}", taskId);
            taskService.completeTask(taskId);
            return new ResponseEntity<>(new ResponseMessageDto("Task completed successfully", HttpStatus.OK), HttpStatus.OK);
        } catch (CustomException e) {
            log.error("Error completing task ID: {}", taskId, e);
            return new ResponseEntity<>(new ResponseMessageDto(e.getMessage(), e.getStatus()), e.getStatus());
        }
    }

    @PostMapping("/{taskId}/incomplete")
    public ResponseEntity<?> incompleteTask(@PathVariable Long taskId) {
        try {
            log.info("Marking task ID: {} as incomplete", taskId);
            taskService.incompleteTask(taskId);
            return new ResponseEntity<>(new ResponseMessageDto("Task marked as incomplete successfully", HttpStatus.OK), HttpStatus.OK);
        } catch (CustomException e) {
            log.error("Error marking task ID: {} as incomplete", taskId, e);
            return new ResponseEntity<>(new ResponseMessageDto(e.getMessage(), e.getStatus()), e.getStatus());
        }
    }

    @GetMapping("/user/{userId}/completed")
    public ResponseEntity<?> getAllCompletedTasks(@PathVariable Long userId) {
        try {
            log.info("Fetching all completed tasks for user ID: {}", userId);
            List<TaskDto> tasks = taskService.getAllCompletedTasks(userId);
            return ResponseEntity.ok(tasks);
        } catch (CustomException e) {
            log.error("Error fetching completed tasks for user ID: {}", userId, e);
            return new ResponseEntity<>(new ResponseMessageDto(e.getMessage(), e.getStatus()), e.getStatus());
        }
    }

    @GetMapping("/user/{userId}/incompleted")
    public ResponseEntity<?> getAllUncompletedTasks(@PathVariable Long userId) {
        try {
            log.info("Fetching all uncompleted tasks for user ID: {}", userId);
            List<TaskDto> tasks = taskService.getAllIncompletedTasks(userId);
            return ResponseEntity.ok(tasks);
        } catch (CustomException e) {
            log.error("Error fetching uncompleted tasks for user ID: {}", userId, e);
            return new ResponseEntity<>(new ResponseMessageDto(e.getMessage(), e.getStatus()), e.getStatus());
        }
    }

    @GetMapping("/user/{userId}/sort/createdAt/asc")
    public ResponseEntity<?> sortTasksByDateCreatedAsc(@PathVariable Long userId) {
        try {
            log.info("Sorting tasks by date created in ascending order for user ID: {}", userId);
            List<TaskDto> tasks = taskService.sortTasksByDateCreatedAsc(userId);
            return ResponseEntity.ok(tasks);
        } catch (CustomException e) {
            log.error("Error sorting tasks by date created in ascending order for user ID: {}", userId, e);
            return new ResponseEntity<>(new ResponseMessageDto(e.getMessage(), e.getStatus()), e.getStatus());
        }
    }

    @GetMapping("/user/{userId}/sort/createdAt/desc")
    public ResponseEntity<?> sortTasksByDateCreatedDesc(@PathVariable Long userId) {
        try {
            log.info("Sorting tasks by date created in descending order for user ID: {}", userId);
            List<TaskDto> tasks = taskService.sortTasksByDateCreatedDesc(userId);
            return ResponseEntity.ok(tasks);
        } catch (CustomException e) {
            log.error("Error sorting tasks by date created in descending order for user ID: {}", userId, e);
            return new ResponseEntity<>(new ResponseMessageDto(e.getMessage(), e.getStatus()), e.getStatus());
        }
    }
}
