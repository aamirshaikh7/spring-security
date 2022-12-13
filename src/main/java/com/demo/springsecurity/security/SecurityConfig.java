package com.demo.springsecurity.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import java.util.concurrent.TimeUnit;

import static com.demo.springsecurity.security.UserRole.*;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public SecurityConfig(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/", "index", "/css/*", "/js/*").permitAll()
                .antMatchers("/api/**").hasRole(EMPLOYEE.name())
                .anyRequest()
                .authenticated()
                .and()
                .formLogin()
                .loginPage("/login").permitAll()
                .defaultSuccessUrl("/courses", true)
                .and()
                .rememberMe()
                    .tokenValiditySeconds((int) TimeUnit.DAYS.toSeconds(7))
                    .key("X4Mosmd+Mmi3PMSu6SxfojpcB5n85VhZQMzoUM+xYTE="); // default: 2 weeks
    }

    // retrieve user from database
    @Override
    @Bean
    protected UserDetailsService userDetailsService() {
        // define user

        UserDetails employee = User.builder()
                .username("john_doe")
                .password(passwordEncoder.encode("password"))
//                .roles(EMPLOYEE.name()) // ROLE_EMPLOYEE
                .authorities(EMPLOYEE.getGrantedAuthorities())
                .build();

        UserDetails admin = User.builder()
                .username("jane_doe")
                .password(passwordEncoder.encode("admin"))
//                .roles(ADMIN.name()) // ROLE_ADMIN
                .authorities(ADMIN.getGrantedAuthorities())
                .build();

        UserDetails trainee = User.builder()
                .username("james_doe")
                .password(passwordEncoder.encode("trainee"))
//                .roles(TRAINEE.name()) // ROLE_TRAINEE
                .authorities(TRAINEE.getGrantedAuthorities())
                .build();

        return new InMemoryUserDetailsManager(employee, admin, trainee);
    }
}