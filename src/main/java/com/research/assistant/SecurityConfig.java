package com.research.assistant;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@Configuration
@EnableWebSecurity
// This allows unauthenticated access to your research API endpoint only.
public class SecurityConfig {
	@Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // disable CSRF for API usage (Postman)
                .authorizeHttpRequests(requests -> requests
                        .requestMatchers("/api/**").permitAll() // permit all API calls
                        .requestMatchers("/", "/home", "/index").permitAll() // allow GET requests from browser
                        .anyRequest().authenticated());
        return http.build();
    }
}