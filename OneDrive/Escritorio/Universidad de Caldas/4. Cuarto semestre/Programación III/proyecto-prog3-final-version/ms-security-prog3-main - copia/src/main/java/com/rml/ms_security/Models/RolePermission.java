package com.rml.ms_security.Models;

import org.springframework.data.mongodb.core.mapping.DBRef;

public class RolePermission {
    private String _id;
    @DBRef
    private Role role;
    @DBRef
    private Permission permission;

    public RolePermission() {

    }

    public String get_id() {
        return _id;
    }


    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Permission getPermission() {
        return permission;
    }

    public void setPermission(Permission permission) {
        this.permission = permission;
    }
}
