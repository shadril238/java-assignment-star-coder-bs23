package com.shadril.taskmanagementjava.service.implementation;

import com.shadril.taskmanagementjava.dto.ResponseMessageDto;
import com.shadril.taskmanagementjava.dto.UserDto;
import com.shadril.taskmanagementjava.entity.User;
import com.shadril.taskmanagementjava.enums.Role;
import com.shadril.taskmanagementjava.exception.CustomException;
import com.shadril.taskmanagementjava.repository.UserRepository;
import com.shadril.taskmanagementjava.service.UserService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class UserServiceImplementation implements UserService, UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private ModelMapper modelMapper;


    @Override
    public UserDto getUserByUsername(String username) throws CustomException {
        User userEntity = userRepository.findByUsername(username).orElse(null);
        if (userEntity == null){
            log.error("User not found");
            throw new CustomException(new ResponseMessageDto("User not found", HttpStatus.NOT_FOUND), HttpStatus.NOT_FOUND);
        }
        log.info("User found with username: {}", username);
        return modelMapper.map(userEntity, UserDto.class);
    }


    @Override
    public UserDto getUserByEmail(String email) throws CustomException{
        User userEntity = userRepository.findByEmail(email).orElse(null);
        if (userEntity == null){
            log.error("User not found");
            throw new CustomException(new ResponseMessageDto("User not found", HttpStatus.NOT_FOUND), HttpStatus.NOT_FOUND);
        }
        log.info("User found with email: {}", email);
        return modelMapper.map(userEntity, UserDto.class);
    }

    @Override
    public UserDto getUserById(Long userId) throws CustomException {
        String errorMessage = "No user found with id: " + userId;
        User userEntity = userRepository
                .findById(userId)
                .orElseThrow(() -> new CustomException(new ResponseMessageDto(errorMessage, HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST));
        log.info("User found with id: {}", userId);
        return new ModelMapper().map(userEntity, UserDto.class);
    }

    @Override
    public UserDto registerUser(UserDto userDto) throws CustomException {
        if(userRepository.findByUsername(userDto.getUsername()).isPresent()){
            log.error("Username already exists");
            throw new CustomException(new ResponseMessageDto("Username already exists", HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
        }
        log.info("Username is available");
        if(userRepository.findByEmail(userDto.getEmail()).isPresent()){
            log.error("Email already exists");
            throw new CustomException(new ResponseMessageDto("Email already exists", HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
        }
        log.info("Email is available");
        User userEntity = modelMapper.map(userDto, User.class);
        userEntity.setPassword(bCryptPasswordEncoder.encode(userDto.getPassword()));
        User storedUserDetails = userRepository.save(userEntity);
        log.info("User registered successfully");
        return modelMapper.map(storedUserDetails, UserDto.class);
    }
    @Override
    public List<UserDto> getAllUsers() throws CustomException {
        List<User> userList;
        try {
            userList = userRepository.findAll();
        } catch (Exception ex) {
            log.error("Error occurred while retrieving users: {}", ex.getMessage());
            throw new CustomException(new ResponseMessageDto("Error occurred while retrieving users", HttpStatus.INTERNAL_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if (userList.isEmpty()) {
            log.error("No users found in the database.");
            throw new CustomException(new ResponseMessageDto("No users found", HttpStatus.NOT_FOUND), HttpStatus.NOT_FOUND);
        }
        log.info("Processing {} users", userList.size());
        return userList.stream()
                .filter(user -> user.getRole() != null && user.getRole().equals(Role.USER))
                .map(user -> modelMapper.map(user, UserDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(String.format("User with username %s not found", username)));

        return new org.springframework.security.core.userdetails.User(user.getUsername(),user.getPassword(),
                true,true,true,true,new ArrayList<>());

    }
}
