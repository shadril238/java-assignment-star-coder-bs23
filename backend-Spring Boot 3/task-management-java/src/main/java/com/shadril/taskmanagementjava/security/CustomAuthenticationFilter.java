package com.shadril.taskmanagementjava.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shadril.taskmanagementjava.SpringApplicationContext;
import com.shadril.taskmanagementjava.constant.AppConstant;
import com.shadril.taskmanagementjava.dto.UserDto;
import com.shadril.taskmanagementjava.dto.UserLoginRequestDto;
import com.shadril.taskmanagementjava.entity.User;
import com.shadril.taskmanagementjava.exception.CustomException;
import com.shadril.taskmanagementjava.service.UserService;
import com.shadril.taskmanagementjava.utils.JwtUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Slf4j
public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        UserLoginRequestDto credential = null;
        try {
            credential = new ObjectMapper().readValue(request.getInputStream(), UserLoginRequestDto.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(credential.getUsername(),credential.getPassword()));
    }

}
