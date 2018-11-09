package ch.epfl.sweng.eventmanager.repository.data;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public final class User {
    private final int id;
    private String name;
    private String email;
    private Set<Permission> permissions;

    public User(int id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.permissions = Collections.emptySet();
    }

    public User(int id, String name, String email, Set<Permission> permissions) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.permissions = Collections.unmodifiableSet(permissions);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public Boolean checkPassword(String password) {
        // FIXME: replace this dumb, hardcoded value by proper logic
        return (password.equals("secret"));
    }

    public boolean hasPermission(Permission permission) {
        // FIXME: replace with proper logic
        return permissions.contains(permission);
    }

    public static enum Permission {
        PUBLISH_NEWS,
        UPDATE_EVENT
    }
}
