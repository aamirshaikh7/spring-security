package com.demo.springsecurity.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import static com.demo.springsecurity.security.UserRole.ADMIN;
import static com.demo.springsecurity.security.UserRole.EMPLOYEE;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public SecurityConfig(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/", "index", "/css/*", "/js/*")
                .permitAll()
                .antMatchers("/api/v1/employees/**").hasRole(EMPLOYEE.name())
                .anyRequest()
                .authenticated()
                .and()
                .httpBasic();
    }

    // retrieve user from database
    @Override
    @Bean
    protected UserDetailsService userDetailsService() {
        // define user

        UserDetails user = User.builder()
                .username("john_doe")
                .password(passwordEncoder.encode("password"))
                .roles(EMPLOYEE.name()) // ROLE_EMPLOYEE
                .build();

        UserDetails admin = User.builder()
                .username("jane_doe")
                .password(passwordEncoder.encode("admin"))
                .roles(ADMIN.name()) // ROLE_ADMIN
                .build();

        return new InMemoryUserDetailsManager(user, admin);
    }
}