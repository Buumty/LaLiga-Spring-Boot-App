package com.laliga.laliga_crud_app.security;

import com.google.common.collect.Sets;

import java.util.Set;

import static com.laliga.laliga_crud_app.security.ApplicationUserPermission.*;

public enum ApplicationUserRole {
    USER(Sets.newHashSet()),
    ADMIN(Sets.newHashSet(USER_READ, USER_WRITE, PLAYER_READ, PLAYER_WRITE)),
    ADMINTRAINEE(Sets.newHashSet(USER_READ, PLAYER_READ));

    private final Set<ApplicationUserPermission> permissions;

    ApplicationUserRole(Set<ApplicationUserPermission> permissions) {
        this.permissions = permissions;
    }
}
