package com.shadril.taskmanagementjava.controller;

import com.shadril.taskmanagementjava.dto.ResponseMessageDto;
import com.shadril.taskmanagementjava.dto.UserDto;
import com.shadril.taskmanagementjava.dto.UserLoginRequestDto;
import com.shadril.taskmanagementjava.dto.UserLoginResponseDto;
import com.shadril.taskmanagementjava.exception.CustomException;
import com.shadril.taskmanagementjava.service.UserService;
import com.shadril.taskmanagementjava.utils.JwtUtils;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/api")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/register")
    public ResponseEntity<ResponseMessageDto> registerUser(@Valid @RequestBody UserDto userDto) throws CustomException {
        log.info("Registering user with username: {}", userDto.getUsername());
        UserDto registeredUser = userService.registerUser(userDto);
        log.info("User registered successfully with username: {}", userDto.getUsername());
        return new ResponseEntity<>(new ResponseMessageDto("User registered successfully", HttpStatus.CREATED), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<UserLoginResponseDto> loginUser(@Valid @RequestBody UserLoginRequestDto userLoginRequestDto) throws CustomException {
        try{
            log.info("Logging in user with username: {}", userLoginRequestDto.getUsername());
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(userLoginRequestDto.getUsername(), userLoginRequestDto.getPassword())
            );

            UserDto userDto = userService.getUserByUsername(userLoginRequestDto.getUsername());
            if(userDto.getIsDeleted()){
                log.error("User with username: {} has been deleted", userLoginRequestDto.getUsername());
                throw new CustomException(new ResponseMessageDto("User not valid", HttpStatus.UNAUTHORIZED), HttpStatus.UNAUTHORIZED);
            }
            List<String> userRoles = new ArrayList<>();
            userRoles.add(String.valueOf(userDto.getRole()));
            String accessToken = JwtUtils.generateToken(userDto.getUsername(), userRoles); // generate token

            UserLoginResponseDto loginResponseDto = new UserLoginResponseDto(
                    "Login successful",
                    HttpStatus.OK,
                    userDto.getId(),
                    userDto.getEmail(),
                    userDto.getUsername(),
                    userDto.getRole(),
                    accessToken
            );
            return new ResponseEntity<>(loginResponseDto, HttpStatus.OK);
        } catch (Exception e){
            log.error("Error occurred while logging in user with username: {}", userLoginRequestDto.getUsername());
            throw new CustomException(new ResponseMessageDto("Invalid username or password", HttpStatus.UNAUTHORIZED), HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserDto>> getAllUsers() throws CustomException {
        log.info("Getting all users");
        List<UserDto> users = userService.getAllUsers();
        log.info("Returning {} users", users.size());
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long userId) throws CustomException {
        log.info("Getting user with id: {}", userId);
        UserDto userDto = userService.getUserById(userId);
        log.info("Returning user with id: {}", userId);
        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }

    @GetMapping("/user/profile")
    public ResponseEntity<UserDto> getCurrentProfile() throws CustomException {
        log.info("Getting user profile");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDto userDto = userService.getUserByUsername(authentication.getName());
        log.info("Returning user profile");
        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }
}
