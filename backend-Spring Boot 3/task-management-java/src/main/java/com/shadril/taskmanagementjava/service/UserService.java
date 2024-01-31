package com.shadril.taskmanagementjava.service;

import com.shadril.taskmanagementjava.dto.UserDto;
import com.shadril.taskmanagementjava.exception.CustomException;

import java.util.List;

public interface UserService {
    UserDto getUserByUsername(String username) throws CustomException;
    UserDto getUserByEmail(String email) throws CustomException;
    UserDto getUserById(Long userId) throws CustomException;
    UserDto registerUser(UserDto userDto) throws CustomException;
    List<UserDto> getAllUsers() throws CustomException;
}
