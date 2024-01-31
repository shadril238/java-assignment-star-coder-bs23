package com.shadril.taskmanagementjava.service.implementation;

import com.shadril.taskmanagementjava.dto.ResponseMessageDto;
import com.shadril.taskmanagementjava.dto.UserDto;
import com.shadril.taskmanagementjava.entity.User;
import com.shadril.taskmanagementjava.exception.CustomException;
import com.shadril.taskmanagementjava.repository.UserRepository;
import com.shadril.taskmanagementjava.service.UserService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

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
            throw new CustomException(new ResponseMessageDto("User not found", HttpStatus.NOT_FOUND), HttpStatus.NOT_FOUND);
        }
        UserDto returnValue = new UserDto();
        BeanUtils.copyProperties(userEntity, returnValue);
        return returnValue;
    }


    @Override
    public UserDto getUserByEmail(String email) throws CustomException{
        User userEntity = userRepository.findByEmail(email).orElse(null);
        if (userEntity == null){
            throw new CustomException(new ResponseMessageDto("User not found", HttpStatus.NOT_FOUND), HttpStatus.NOT_FOUND);
        }
        UserDto returnValue = new UserDto();
        BeanUtils.copyProperties(userEntity, returnValue);
        return returnValue;
    }

    @Override
    public UserDto getUserById(Long userId) throws CustomException {
        return null;
    }

    @Override
    public UserDto registerUser(UserDto userDto) throws CustomException {
        if(userRepository.findByUsername(userDto.getUsername()).isPresent()){
            throw new CustomException(new ResponseMessageDto("Username already exists", HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
        }
        if(userRepository.findByEmail(userDto.getEmail()).isPresent()){
            throw new CustomException(new ResponseMessageDto("Email already exists", HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
        }
        User userEntity = modelMapper.map(userDto, User.class);
        userEntity.setPassword(bCryptPasswordEncoder.encode(userDto.getPassword()));
        User storedUserDetails = userRepository.save(userEntity);

        return modelMapper.map(storedUserDetails, UserDto.class);
    }

    @Override
    public UserDto updateUser(UserDto userDto) throws CustomException {
        if (userDto.getId() == null) {
            throw new CustomException(new ResponseMessageDto("User id is required", HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
        }

        User existingUser = userRepository.findById(userDto.getId())
                .orElseThrow(() -> new CustomException(new ResponseMessageDto("User not found", HttpStatus.NOT_FOUND), HttpStatus.NOT_FOUND));

        Optional<User> existingUserByUsername = userRepository.findByUsername(userDto.getUsername());

        if (existingUserByUsername.isPresent() && !existingUserByUsername.get().getId().equals(userDto.getId())) {
            throw new CustomException(new ResponseMessageDto("Username already taken", HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
        }

        Optional<User> existingUserByEmail = userRepository.findByEmail(userDto.getEmail());
        if (existingUserByEmail.isPresent() && !existingUserByEmail.get().getId().equals(userDto.getId())) {
            throw new CustomException(new ResponseMessageDto("Email already taken", HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
        }

        try {
            modelMapper.typeMap(UserDto.class, User.class).addMappings(mapper -> {
                mapper.skip(User::setId);
                mapper.skip(User::setPassword);
            });
            modelMapper.map(userDto, existingUser);

            if (userDto.getPassword() != null && !userDto.getPassword().isEmpty()) {
                existingUser.setPassword(bCryptPasswordEncoder.encode(userDto.getPassword()));
            }
            User updatedUser = userRepository.save(existingUser);

            UserDto returnValue = modelMapper.map(updatedUser, UserDto.class);
            returnValue.setPassword(null);
            return returnValue;
        } catch (Exception e) {
            log.error("Error updating user: ", e);
            throw new CustomException(new ResponseMessageDto("Error updating user", HttpStatus.INTERNAL_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }


    @Override
    public void deleteUser(Long userId) throws CustomException {

    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return null;
    }
}
