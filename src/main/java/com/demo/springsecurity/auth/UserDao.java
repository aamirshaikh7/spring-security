package com.demo.springsecurity.auth;

import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserDao {
    Optional<User> getUserByUsername(String username);
}
