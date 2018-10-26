package ch.epfl.sweng.eventmanager.data;

public final class User {
    private final int id;
    private String name;
    private String email;

    public User(int id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
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
}
