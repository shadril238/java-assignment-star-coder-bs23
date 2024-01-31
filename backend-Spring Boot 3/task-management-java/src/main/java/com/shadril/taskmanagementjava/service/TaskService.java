package com.shadril.taskmanagementjava.service;

import com.shadril.taskmanagementjava.dto.TaskDto;
import com.shadril.taskmanagementjava.exception.CustomException;

import java.util.List;

public interface TaskService {
    void createTask(TaskDto taskDto) throws CustomException;
    TaskDto updateTask(Long taskId, TaskDto taskDto) throws CustomException;
    void deleteTask(Long taskId) throws CustomException;
    List<TaskDto> getAllTasksByUserId(Long userId) throws CustomException;
    TaskDto getTaskById(Long taskId) throws CustomException;
    void completeTask(Long taskId) throws CustomException;
    void incompleteTask(Long taskId) throws CustomException;
    List<TaskDto> getAllCompletedTasks(Long userId) throws CustomException;
    List<TaskDto> getAllIncompletedTasks(Long userId) throws CustomException;
    List<TaskDto> getAllTasks() throws CustomException;
    List<TaskDto> sortTasksByDateCreatedAsc(Long userId) throws CustomException;
    List<TaskDto> sortTasksByDateCreatedDesc(Long userId) throws CustomException;
}
