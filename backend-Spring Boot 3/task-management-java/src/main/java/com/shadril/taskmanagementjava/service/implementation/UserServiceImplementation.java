package com.shadril.taskmanagementjava.service.implementation;

import com.shadril.taskmanagementjava.dto.ResponseMessageDto;
import com.shadril.taskmanagementjava.dto.UserDto;
import com.shadril.taskmanagementjava.entity.User;
import com.shadril.taskmanagementjava.exception.CustomException;
import com.shadril.taskmanagementjava.repository.UserRepository;
import com.shadril.taskmanagementjava.service.UserService;
import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@Transactional
public class UserServiceImplementation implements UserService, UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;


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
    public UserDto getUserByUserId(Long userId) throws CustomException{
        User userEntity = userRepository.findById(userId).orElse(null);
        if (userEntity == null){
            throw new CustomException(new ResponseMessageDto("User not found", HttpStatus.NOT_FOUND), HttpStatus.NOT_FOUND);
        }
        UserDto returnValue = new UserDto();
        BeanUtils.copyProperties(userEntity, returnValue);
        return returnValue;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User userEntity = userRepository.findByUsername(username).orElse(null);
        if (userEntity == null) throw new UsernameNotFoundException("User not found");
        return new org.springframework.security.core.userdetails.User(
                userEntity.getUsername(),
                userEntity.getPassword(),
                true, true, true, true,
                new ArrayList<>()
        );
    }
}
