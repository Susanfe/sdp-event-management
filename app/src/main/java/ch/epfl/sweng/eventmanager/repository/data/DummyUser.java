package ch.epfl.sweng.eventmanager.repository.data;

public final class DummyUser implements User {
    String uid;
    String displayName;

    public DummyUser(String uid, String displayName) {
        this.uid = uid;
    }

    @Override
    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String getEmail() {
        return uid;
    }

}
