package com.shadril.taskmanagementjava.security;

import com.shadril.taskmanagementjava.constant.AppConstant;
import com.shadril.taskmanagementjava.utils.JwtUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
public class CustomAuthorizationFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String header = request.getHeader(AppConstant.HEADER_STRING);
        if(header==null||!header.startsWith(AppConstant.TOKEN_PREFIX)){
            filterChain.doFilter(request,response);
        }else {
            UsernamePasswordAuthenticationToken authenticationToken = null;
            try {
                authenticationToken = getAuthenticationToken(header);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            filterChain.doFilter(request,response);
        }
    }

    public UsernamePasswordAuthenticationToken getAuthenticationToken(String header) throws Exception {
        if(header != null){
            String token = header.replace(AppConstant.TOKEN_PREFIX, "");
            String user = JwtUtils.hasTokenExpired(token) ? null : JwtUtils.extractUser(token);

            if (user != null) {
                List<String> userRoles = Collections.singletonList(JwtUtils.extractUser(token));
                List<GrantedAuthority> authorities = new ArrayList<>();
                for (String role : userRoles) {
                    authorities.add(new SimpleGrantedAuthority(role));
                }
                return new UsernamePasswordAuthenticationToken(user, null, authorities);
            }
        }
        return null;
    }
}
