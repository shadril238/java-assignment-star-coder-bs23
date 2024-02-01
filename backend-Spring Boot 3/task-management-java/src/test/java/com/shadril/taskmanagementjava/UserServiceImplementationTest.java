package com.shadril.taskmanagementjava;

import com.shadril.taskmanagementjava.dto.UserDto;
import com.shadril.taskmanagementjava.entity.User;
import com.shadril.taskmanagementjava.exception.CustomException;
import com.shadril.taskmanagementjava.repository.UserRepository;
import com.shadril.taskmanagementjava.service.implementation.UserServiceImplementation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplementationTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private UserServiceImplementation userService;

    @Test
    public void getUserByUsernameSuccessTest() throws CustomException {
        User userEntity = new User();
        userEntity.setUsername("testUser");
        UserDto userDto = new UserDto();
        userDto.setUsername("testUser");

        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(userEntity));
        when(modelMapper.map(userEntity, UserDto.class)).thenReturn(userDto);

        UserDto retrievedUser = userService.getUserByUsername("testUser");

        assertEquals(userDto.getUsername(), retrievedUser.getUsername());
    }

    @Test
    public void registerUserSuccessTest() throws CustomException {
        UserDto userDto = new UserDto();
        userDto.setUsername("newUser");
        userDto.setPassword("Password123!");
        userDto.setEmail("newuser@example.com");
        userDto.setFirstname("New");
        userDto.setLastname("User");
        userDto.setBirthdate(LocalDate.of(1990, 1, 1));

        User userEntity = new User();

        when(userRepository.findByUsername(userDto.getUsername())).thenReturn(Optional.empty());
        when(userRepository.findByEmail(userDto.getEmail())).thenReturn(Optional.empty());
        when(bCryptPasswordEncoder.encode(userDto.getPassword())).thenReturn("EncodedPassword");
        when(modelMapper.map(userDto, User.class)).thenReturn(userEntity);
        when(userRepository.save(userEntity)).thenReturn(userEntity);
        when(modelMapper.map(userEntity, UserDto.class)).thenReturn(userDto);

        UserDto registeredUser = userService.registerUser(userDto);

        assertNotNull(registeredUser);
        assertEquals(userDto.getUsername(), registeredUser.getUsername());
    }
}
