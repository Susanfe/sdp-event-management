package ch.epfl.sweng.eventmanager.repository.data;

public final class User {
    private final int id;
    private String name;
    private String mail;

    public User(int id, String name, String mail) {
        this.id = id;
        this.name = name;
        this.mail = mail;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getMail() {
        return mail;
    }

    public Boolean checkPassword(String password) {
        // FIXME: replace this dumb, hardcoded value by proper logic
        return (password.equals("secret"));
    }
}
