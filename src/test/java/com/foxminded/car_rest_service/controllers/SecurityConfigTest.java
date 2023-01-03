package com.foxminded.car_rest_service.controllers;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

import static com.foxminded.car_rest_service.security.Role.ADMIN;
import static com.foxminded.car_rest_service.security.Role.STAFF;
import static com.foxminded.car_rest_service.security.Role.USER;

@TestConfiguration
public class SecurityConfigTest {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .mvcMatchers(HttpMethod.GET, "/**").hasAnyRole(USER.toString(), ADMIN.toString(), STAFF.toString())
                .mvcMatchers(HttpMethod.PUT, "/**").hasAnyRole(ADMIN.toString(), STAFF.toString())
                .mvcMatchers(HttpMethod.POST, "/**").hasAnyRole(ADMIN.toString())
                .mvcMatchers(HttpMethod.DELETE, "/**").hasAnyRole(ADMIN.toString())
                .anyRequest().authenticated()
                .and().build();
    }
}
