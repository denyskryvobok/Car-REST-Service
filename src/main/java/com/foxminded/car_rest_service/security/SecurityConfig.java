package com.foxminded.car_rest_service.security;

import com.foxminded.car_rest_service.dao.AppUserDAO;
import com.foxminded.car_rest_service.security.entities.AppUser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static java.lang.String.format;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http.csrf().disable()
                .authorizeRequests()
                    .mvcMatchers(HttpMethod.GET, "/**").permitAll()
                    .anyRequest().authenticated()
                .and()
                    .httpBasic()
                .and()
                    .build();
    }

    @Bean
    public UserDetailsService userDetailsService(AppUserDAO appUserDAO) {
        return username -> {
            AppUser user = appUserDAO.findByUsernameWithRoles(username)
                    .orElseThrow(() -> new UsernameNotFoundException(format("User with username(%s) wasn't found", username)));

            var roles = user.getAppUserRoles().stream()
                                              .map(appUserRole -> appUserRole.getRole().getName())
                                              .toArray(String[]::new);

            return User.builder()
                        .username(user.getUsername())
                        .password(user.getPassword())
                        .roles(roles)
                        .disabled(user.getDisabled())
                    .build();
        };
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
