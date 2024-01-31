package com.shadril.taskmanagementjava.service.implementation;

import com.shadril.taskmanagementjava.dto.ResponseMessageDto;
import com.shadril.taskmanagementjava.dto.TaskDto;
import com.shadril.taskmanagementjava.entity.Task;
import com.shadril.taskmanagementjava.entity.User;
import com.shadril.taskmanagementjava.enums.TaskStatus;
import com.shadril.taskmanagementjava.exception.CustomException;
import com.shadril.taskmanagementjava.repository.TaskRepository;
import com.shadril.taskmanagementjava.repository.UserRepository;
import com.shadril.taskmanagementjava.service.TaskService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
public class TaskServiceImplementation implements TaskService {
    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public void createTask(TaskDto taskDto) throws CustomException {
        try{
            log.info("Creating a new task for user ID: {}", taskDto.getUserId());
            User user = userRepository.findByIdAndIsDeletedFalse(taskDto.getUserId())
                    .orElseThrow(() -> new CustomException(new ResponseMessageDto("User not found", HttpStatus.NOT_FOUND), HttpStatus.NOT_FOUND));
            Task task = modelMapper.map(taskDto, Task.class);
            task.setUser(user);
            task.setCreatedAt(LocalDateTime.now());
            task.setIsCompleted(false);
            task.setIsDeleted(false);
            Task savedTask = taskRepository.save(task);
            log.info("Task created successfully with ID: {}", savedTask.getId());
        } catch (Exception e) {
            log.error("Error occurred while creating task: {}", e.getMessage());
            throw new CustomException(new ResponseMessageDto("Error occurred while creating task", HttpStatus.INTERNAL_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public TaskDto updateTask(Long taskId, TaskDto taskDto) throws CustomException {
        log.info("Inside updateTask method in TaskServiceImplementation");
        try {
            Task task = taskRepository.findByIdAndIsDeletedFalse(taskId)
                    .orElseThrow(() -> new CustomException(new ResponseMessageDto("Task not found", HttpStatus.NOT_FOUND), HttpStatus.NOT_FOUND));
            log.info("Task found with ID: {}", taskId);

            if (taskDto.getUserId() != null) {
                User user = userRepository.findByIdAndIsDeletedFalse(taskDto.getUserId())
                        .orElseThrow(() -> new CustomException(new ResponseMessageDto("User not found", HttpStatus.NOT_FOUND), HttpStatus.NOT_FOUND));
                task.setUser(user);
            }

            if (taskDto.getTitle() != null) task.setTitle(taskDto.getTitle());
            if (taskDto.getDescription() != null) task.setDescription(taskDto.getDescription());
            if (taskDto.getStatus() != null) task.setStatus(taskDto.getStatus());
            if (taskDto.getIsCompleted() != null) task.setIsCompleted(taskDto.getIsCompleted());

            task.setUpdatedAt(LocalDateTime.now());

            Task updatedTask = taskRepository.save(task);
            log.info("Task updated successfully with ID: {}", updatedTask.getId());

            // Convert the updated task back to DTO and return
            return modelMapper.map(updatedTask, TaskDto.class);
        } catch (Exception e) {
            log.error("Error occurred while updating task: {}", e.getMessage());
            throw new CustomException(new ResponseMessageDto("Error occurred while updating task", HttpStatus.INTERNAL_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @Override
    public void deleteTask(Long taskId) throws CustomException {
        log.info("Inside deleteTask method in TaskServiceImplementation");
        try {
            Task task = taskRepository.findByIdAndIsDeletedFalse(taskId)
                    .orElseThrow(() -> new CustomException(new ResponseMessageDto("Task not found", HttpStatus.NOT_FOUND), HttpStatus.NOT_FOUND));

            task.setIsDeleted(true);
            task.setUpdatedAt(LocalDateTime.now());

            taskRepository.save(task);
            log.info("Task with ID: {} marked as deleted", taskId);
        } catch (Exception e) {
            log.error("Error occurred while deleting task: {}", e.getMessage());
            throw new CustomException(new ResponseMessageDto("Error occurred while deleting task", HttpStatus.INTERNAL_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public List<TaskDto> getAllTasksByUserId(Long userId) throws CustomException {
        userRepository.findByIdAndIsDeletedFalse(userId)
                .orElseThrow(() -> new CustomException(new ResponseMessageDto("User not found", HttpStatus.NOT_FOUND), HttpStatus.NOT_FOUND));

        List<Task> tasks = taskRepository.findAllByUserIdAndIsDeletedFalse(userId);
        return tasks.stream().map(task -> modelMapper.map(task, TaskDto.class)).collect(Collectors.toList());
    }

    @Override
    public TaskDto getTaskById(Long taskId) throws CustomException {
        Task task = taskRepository.findByIdAndIsDeletedFalse(taskId)
                .orElseThrow(() -> new CustomException(new ResponseMessageDto("Task not found", HttpStatus.NOT_FOUND), HttpStatus.NOT_FOUND));

        return modelMapper.map(task, TaskDto.class);
    }


    @Override
    public void completeTask(Long taskId) throws CustomException {
        Task task = taskRepository.findByIdAndIsDeletedFalse(taskId)
                .orElseThrow(() -> new CustomException(new ResponseMessageDto("Task not found", HttpStatus.NOT_FOUND), HttpStatus.NOT_FOUND));
        task.setIsCompleted(true);
        task.setStatus(TaskStatus.COMPLETED);
        task.setUpdatedAt(LocalDateTime.now());
        taskRepository.save(task);
    }

    @Override
    public void incompleteTask(Long taskId) throws CustomException {
        Task task = taskRepository.findByIdAndIsDeletedFalse(taskId)
                .orElseThrow(() -> new CustomException(new ResponseMessageDto("Task not found", HttpStatus.NOT_FOUND), HttpStatus.NOT_FOUND));
        task.setIsCompleted(false);
        task.setStatus(TaskStatus.TODO);
        task.setUpdatedAt(LocalDateTime.now());
        taskRepository.save(task);
    }


    @Override
    public List<TaskDto> getAllCompletedTasks(Long userId) {
        List<Task> tasks = taskRepository.findAllByUserIdAndIsCompletedTrueAndIsDeletedFalse(userId);
        return tasks.stream()
                .map(task -> modelMapper.map(task, TaskDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<TaskDto> getAllIncompletedTasks(Long userId) {
        List<Task> tasks = taskRepository.findAllByUserIdAndIsCompletedFalseAndIsDeletedFalse(userId);
        return tasks.stream()
                .map(task -> modelMapper.map(task, TaskDto.class))
                .collect(Collectors.toList());
    }


    @Override
    public List<TaskDto> getAllTasks() {
        List<Task> tasks = taskRepository.findAllByIsDeletedFalse();
        return tasks.stream()
                .map(task -> modelMapper.map(task, TaskDto.class))
                .collect(Collectors.toList());
    }


    @Override
    public List<TaskDto> sortTasksByDateCreatedAsc(Long userId) {
        List<Task> tasks = taskRepository.findAllByUserIdAndIsDeletedFalseOrderByCreatedAtAsc(userId);
        return tasks.stream()
                .map(task -> modelMapper.map(task, TaskDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<TaskDto> sortTasksByDateCreatedDesc(Long userId) {
        List<Task> tasks = taskRepository.findAllByUserIdAndIsDeletedFalseOrderByCreatedAtDesc(userId);
        return tasks.stream()
                .map(task -> modelMapper.map(task, TaskDto.class))
                .collect(Collectors.toList());
    }
}
