package ch.epfl.sweng.eventmanager.repository.data;

/**
 * Dummy user class, only used in tests.
 */
public final class DummyUser implements User {
    String uid;
    String displayName;
    String email;

    public DummyUser(String uid, String displayName, String email) {
        this.uid = uid;
        this.displayName = displayName;
        this.email = email;
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
    public String getEmail() {
        return email;
    }

}
