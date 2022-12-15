package com.demo.springsecurity.auth;

import com.google.common.collect.Lists;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.demo.springsecurity.security.UserRole.*;

@Repository
public class UserDaoImpl implements UserDao {

    private final PasswordEncoder passwordEncoder;

    public UserDaoImpl(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Optional<User> getUserByUsername(String username) {
        return getUsers().stream().filter(user ->
                username.equals(user.getUsername())).findFirst();
    }

    private List<User> getUsers() {
        return Lists.newArrayList(
                new User("john_doe", passwordEncoder.encode("password"), EMPLOYEE.getGrantedAuthorities(), true, true, true, true),
                new User("jane_doe", passwordEncoder.encode("admin"), ADMIN.getGrantedAuthorities(), true, true, true, true),
                new User("james_doe", passwordEncoder.encode("trainee"), TRAINEE.getGrantedAuthorities(), true, true, true, true)
        );
    }
}
