package com.shadril.taskmanagementjava.service;

import com.shadril.taskmanagementjava.dto.UserDto;
import com.shadril.taskmanagementjava.exception.CustomException;

public interface UserService {
    UserDto getUserByUsername(String username) throws CustomException;
    UserDto getUserByEmail(String email) throws CustomException;
    UserDto getUserByUserId(Long userId) throws CustomException;
}
