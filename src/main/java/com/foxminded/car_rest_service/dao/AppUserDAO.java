package com.foxminded.car_rest_service.dao;

import com.foxminded.car_rest_service.security.entities.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface AppUserDAO extends JpaRepository<AppUser, Long> {

    @Query(value = "SELECT u " +
                   "FROM AppUser u " +
                   "JOIN FETCH u.appUserRoles ur " +
                   "JOIN FETCH ur.role " +
                   "WHERE u.username = :username ")
    Optional<AppUser> findByUsernameWithRoles(String username);
}
