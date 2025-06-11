package com.example.Real_time.Auction.System.config;

import com.example.Real_time.Auction.System.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private UserService userService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/register", "/ws/**", "/swagger-ui/**", "/api-docs/**", "/items/**").permitAll()
                        .anyRequest().authenticated()
                )
                .httpBasic(withDefaults()) // Modern replacement for httpBasic()
                .csrf(csrf -> csrf.disable()) // Modern CSRF configuration
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // Optional: Stateless for REST APIs
                );
        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return userService;
    }
}