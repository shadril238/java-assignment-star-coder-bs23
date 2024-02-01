package com.shadril.taskmanagementjava.security;

import com.shadril.taskmanagementjava.enums.Role;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        //config.addAllowedOrigin("*");
        config.addAllowedOrigin("http://localhost:5173");
        //config.addAllowedHeader("Authorization");
        config.setAllowedHeaders(Arrays.asList(
                "Authorization",
                "Content-Type",
                "Accept",
                "X-Requested-With",
                "Cache-Control"
        ));
        config.addAllowedMethod("GET");
        config.addAllowedMethod("POST");
        config.addAllowedMethod("PUT");
        config.addAllowedMethod("DELETE");
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, AuthenticationManager authenticationManager)
            throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth->{
                    auth
                            .requestMatchers(HttpMethod.POST, "/api/register").permitAll()
                            .requestMatchers(HttpMethod.POST, "/api/login").permitAll()
                            // User
                            .requestMatchers(HttpMethod.GET, "/api/users").hasAuthority(Role.ADMIN.name())
                            .requestMatchers(HttpMethod.GET, "/api/users/**").hasAuthority(Role.ADMIN.name())
                            .requestMatchers(HttpMethod.POST, "/api/user/profile").hasAnyAuthority(Role.ADMIN.name(), Role.USER.name())
                            .requestMatchers(HttpMethod.PUT, "/api/user/update").hasAnyAuthority(Role.ADMIN.name(), Role.USER.name())
                            // Task
                            .requestMatchers(HttpMethod.POST, "/api/tasks/").hasAnyAuthority(Role.USER.name(), Role.ADMIN.name())
                            .requestMatchers(HttpMethod.GET, "/api/tasks/").hasAuthority(Role.ADMIN.name())
                            .requestMatchers(HttpMethod.PUT, "/api/tasks/{taskId}").hasAnyAuthority(Role.USER.name(), Role.ADMIN.name())
                            .requestMatchers(HttpMethod.DELETE, "/api/tasks/{taskId}").hasAnyAuthority(Role.USER.name())
                            .requestMatchers(HttpMethod.GET, "/api/tasks/user/{userId}").hasAnyAuthority(Role.ADMIN.name(), Role.USER.name())
                            .requestMatchers(HttpMethod.GET, "/api/tasks/{taskId}").hasAnyAuthority(Role.ADMIN.name(), Role.USER.name())
                            .requestMatchers(HttpMethod.POST, "/api/tasks/{taskId}/complete").hasAnyAuthority(Role.USER.name())
                            .requestMatchers(HttpMethod.POST, "/api/tasks/{taskId}/incomplete").hasAnyAuthority(Role.USER.name())
                            .requestMatchers(HttpMethod.GET, "/api/tasks/user/{userId}/completed").hasAnyAuthority(Role.USER.name())
                            .requestMatchers(HttpMethod.GET, "/api/tasks/user/{userId}/incompleted").hasAnyAuthority(Role.USER.name())
                            .requestMatchers(HttpMethod.GET, "/api/tasks/user/{userId}/sort/createdAt/asc").hasAnyAuthority(Role.USER.name())
                            .requestMatchers(HttpMethod.GET, "/api/tasks/user/{userId}/sort/createdAt/desc").hasAnyAuthority(Role.USER.name())

                            .anyRequest().authenticated();
                })
                .addFilter(new CustomAuthenticationFilter(authenticationManager))
                .addFilterBefore(new CustomAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class)
        ;
        return http.build();
    }
}
