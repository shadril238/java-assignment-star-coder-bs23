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
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Slf4j
public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        try {
            UserLoginRequestDto credential = new ObjectMapper().readValue(request.getInputStream(), UserLoginRequestDto.class);
            return authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(credential.getUsername(),credential.getPassword())
            );
        } catch (IOException e) {
            log.info("Exception occurred at attemptAuthentication method: {}",e.getLocalizedMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        String username = ((User)authResult.getPrincipal()).getUsername();
        String accessToken = JwtUtils.generateToken(username);
        UserService userService = (UserService) SpringApplicationContext.getBean("userServiceImplementation");
        UserDto userDto = null;
        try {
            userDto = userService.getUserByUsername(username);
        } catch (CustomException e) {
            throw new RuntimeException(e);
        }

        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("userId", userDto.getId());
        responseBody.put("userName", userDto.getUsername());
        responseBody.put(AppConstant.HEADER_STRING, AppConstant.TOKEN_PREFIX + accessToken);
        ObjectMapper objectMapper = new ObjectMapper();
        String responseBodyJson = objectMapper.writeValueAsString(responseBody);
        response.setContentType("application/json");
        response.getWriter().write(responseBodyJson);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("error", "Authentication failed");
        errorResponse.put("message", "Invalid email or password");
        ObjectMapper objectMapper = new ObjectMapper();
        String errorResponseJson = objectMapper.writeValueAsString(errorResponse);
        response.getWriter().write(errorResponseJson);
    }

}
