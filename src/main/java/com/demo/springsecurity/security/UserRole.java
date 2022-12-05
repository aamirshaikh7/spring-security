package com.demo.springsecurity.security;

import com.google.common.collect.Sets;

import java.util.Set;

import static com.demo.springsecurity.security.UserPermission.*;

public enum UserRole {
    EMPLOYEE(Sets.newHashSet()),
    ADMIN(Sets.newHashSet(EMPLOYEE_READ, EMPLOYEE_WRITE, COURSE_READ, COURSE_WRITE));

    private final Set<UserPermission> permissions;

    UserRole(Set<UserPermission> permissions) {
        this.permissions = permissions;
    }

    public Set<UserPermission> getPermissions() {
        return permissions;
    }
}