package com.tigercel.hearthstone.model.support;

import lombok.Data;

/**
 * Created by somebody on 2016/8/1.
 */
public enum UserRole {
    ROLE_ADMIN("ROLE_ADMIN"),
    ROLE_USER("ROLE_USER");

    private String role;

    UserRole(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getId(){
        return name();
    }

    @Override
    public String toString() {
        return role;
    }
}