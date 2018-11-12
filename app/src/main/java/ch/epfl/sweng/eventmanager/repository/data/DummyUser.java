package ch.epfl.sweng.eventmanager.repository.data;

import java.util.Set;

/**
 * Dummy user class, only used in tests.
 */
public final class DummyUser implements User {
    String uid;
    String displayName;
    String email;
    Set<Permission> permissions;

    public DummyUser(String uid, String displayName, String email, Set<Permission> permissions) {
        this.uid = uid;
        this.displayName = displayName;
        this.email = email;
        this.permissions = permissions;
    }

    @Override
    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String getUid() {
        return uid;
    }

    @Override
    public boolean hasPermission(Permission permission) {
        return permissions.contains(permission);
    }

    @Override
    public String getEmail() {
        return email;
    }

}
