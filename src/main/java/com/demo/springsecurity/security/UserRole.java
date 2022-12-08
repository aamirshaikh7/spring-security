package com.demo.springsecurity.security;

import com.google.common.collect.Sets;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;

import static com.demo.springsecurity.security.UserPermission.*;

public enum UserRole {
    EMPLOYEE(Sets.newHashSet()),
    ADMIN(Sets.newHashSet(EMPLOYEE_READ, EMPLOYEE_WRITE, COURSE_READ, COURSE_WRITE)),
    TRAINEE(Sets.newHashSet(EMPLOYEE_READ, COURSE_READ));

    private final Set<UserPermission> permissions;

    UserRole(Set<UserPermission> permissions) {
        this.permissions = permissions;
    }

    public Set<UserPermission> getPermissions() {
        return permissions;
    }

    public Set<SimpleGrantedAuthority> getGrantedAuthorities() {
        Set<SimpleGrantedAuthority> collect = getPermissions().stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toSet());

        collect.add(new SimpleGrantedAuthority("ROLE_" + this.name()));

        return collect;
    }
}