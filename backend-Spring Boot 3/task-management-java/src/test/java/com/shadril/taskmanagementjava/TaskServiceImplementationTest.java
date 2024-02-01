package com.shadril.taskmanagementjava;

import com.shadril.taskmanagementjava.dto.TaskDto;
import com.shadril.taskmanagementjava.entity.Task;
import com.shadril.taskmanagementjava.entity.User;
import com.shadril.taskmanagementjava.enums.Role;
import com.shadril.taskmanagementjava.enums.TaskStatus;
import com.shadril.taskmanagementjava.exception.CustomException;
import com.shadril.taskmanagementjava.repository.TaskRepository;
import com.shadril.taskmanagementjava.repository.UserRepository;
import com.shadril.taskmanagementjava.service.implementation.TaskServiceImplementation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TaskServiceImplementationTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private TaskServiceImplementation taskService;

    @Test
    public void testCreateTaskSuccess() throws CustomException {

        TaskDto taskDto = TaskDto.builder()
                .title("Sample Task")
                .description("This is a sample task description.")
                .createdAt(LocalDateTime.now())
                .userId(1L)
                .status(TaskStatus.TODO)
                .isCompleted(false)
                .build();

        User user = User.builder()
                .id(1L)
                .username("testUser")
                .email("test@example.com")
                .password("password123")
                .firstname("Test")
                .lastname("User")
                .birthdate(LocalDate.of(1990, 1, 1))
                .role(Role.USER)
                .isDeleted(false)
                .build();

        Task task = new Task();
        task.setTitle(taskDto.getTitle());
        task.setDescription(taskDto.getDescription());
        task.setCreatedAt(taskDto.getCreatedAt());
        task.setUser(user);
        task.setStatus(taskDto.getStatus());
        task.setIsCompleted(taskDto.getIsCompleted());

        when(userRepository.findByIdAndIsDeletedFalse(taskDto.getUserId())).thenReturn(Optional.of(user));
        when(modelMapper.map(taskDto, Task.class)).thenReturn(task);
        when(taskRepository.save(task)).thenReturn(task);
        when(modelMapper.map(task, TaskDto.class)).thenReturn(taskDto);

        TaskDto result = taskService.createTask(taskDto);

        assertNotNull(result);
        assertEquals("Sample Task", result.getTitle());
        assertEquals("This is a sample task description.", result.getDescription());
        assertEquals(TaskStatus.TODO, result.getStatus());
        assertFalse(result.getIsCompleted());
    }
    @Test
    public void testUpdateTaskSuccess() throws CustomException {
        Long taskId = 1L;
        TaskDto taskDto = new TaskDto();
        taskDto.setTitle("Updated Title");
        taskDto.setDescription("Updated Description");

        Task existingTask = new Task();
        existingTask.setId(taskId);
        existingTask.setTitle("Original Title");
        existingTask.setDescription("Original Description");

        when(taskRepository.findByIdAndIsDeletedFalse(taskId)).thenReturn(Optional.of(existingTask));
        when(taskRepository.save(any(Task.class))).thenReturn(existingTask);
        when(modelMapper.map(existingTask, TaskDto.class)).thenReturn(taskDto);

        TaskDto result = taskService.updateTask(taskId, taskDto);

        assertNotNull(result);
        assertEquals("Updated Title", result.getTitle());
        assertEquals("Updated Description", result.getDescription());
    }

    @Test
    public void testDeleteTaskSuccess() throws CustomException {
        Long taskId = 1L;
        Task task = new Task();
        task.setId(taskId);
        task.setIsDeleted(false);

        when(taskRepository.findByIdAndIsDeletedFalse(taskId)).thenReturn(Optional.of(task));

        taskService.deleteTask(taskId);

        verify(taskRepository).save(task);
        assertTrue(task.getIsDeleted());
    }

    @Test
    public void testGetAllTasksByUserIdSuccess() throws CustomException {
        Long userId = 1L;
        List<Task> tasks = new ArrayList<>();
        Task task1 = new Task();
        Task task2 = new Task();
        tasks.add(task1);
        tasks.add(task2);

        when(userRepository.findByIdAndIsDeletedFalse(userId)).thenReturn(Optional.of(new User()));
        when(taskRepository.findAllByUserIdAndIsDeletedFalse(userId)).thenReturn(tasks);
        when(modelMapper.map(any(Task.class), eq(TaskDto.class))).thenReturn(new TaskDto());

        List<TaskDto> result = taskService.getAllTasksByUserId(userId);

        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    public void testGetTaskByIdSuccess() throws CustomException {
        Long taskId = 1L;
        Task task = new Task();
        task.setId(taskId);

        when(taskRepository.findByIdAndIsDeletedFalse(taskId)).thenReturn(Optional.of(task));
        when(modelMapper.map(task, TaskDto.class)).thenReturn(new TaskDto());

        TaskDto result = taskService.getTaskById(taskId);

        assertNotNull(result);
    }

    @Test
    public void testCompleteTaskSuccess() throws CustomException {
        Long taskId = 1L;
        Task task = new Task();
        task.setId(taskId);
        task.setIsCompleted(false);

        when(taskRepository.findByIdAndIsDeletedFalse(taskId)).thenReturn(Optional.of(task));

        taskService.completeTask(taskId);

        verify(taskRepository).save(task);
        assertTrue(task.getIsCompleted());
        assertEquals(TaskStatus.COMPLETED, task.getStatus());
    }
}
