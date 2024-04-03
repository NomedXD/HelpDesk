package com.innowise.domain;

import org.springframework.security.core.GrantedAuthority;

public enum UserRole implements GrantedAuthority {
    ROLE_MANAGER,
    ROLE_EMPLOYEE,
    ROLE_ENGINEER;

    @Override
    public String getAuthority() {
        return this.name();
    }
}
