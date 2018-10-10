package ch.epfl.sweng.eventmanager.userManagement;

import javax.inject.Singleton;
import ch.epfl.sweng.eventmanager.repository.data.User;

/**
 * This singleton class keeps the state of the current user.
 */
@Singleton
public class InMemorySession {
    private static InMemorySession instance = null;
    private User user;
    private String token;

    void setCurrentUser(User user){
        this.user = user;
    }

    void reset(){
        setCurrentUser(null);
    }

    static InMemorySession getInstance() {
        if (instance == null) {
            instance = new InMemorySession();
        }

        return instance;
    }

    public User getUser() {
        return user;
    }
}
