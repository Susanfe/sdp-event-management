package ch.epfl.sweng.eventmanager;

import android.os.Build;
import android.support.annotation.RequiresApi;

import java.util.Optional;

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

    private void setCurrentUser(User user){
        this.user = user;
    }

    private void reset(){
        setCurrentUser(null);
    }

    /**
     * Get the unique InMemorySession object, global to the application.
     * @return the local InMemorySession instance
     */
    public static InMemorySession getInstance() {
        if (instance == null) {
            instance = new InMemorySession();
        }

        return instance;
    }

    /**
     * @return true if an user is logged in
     */
    public boolean isLoggedIn() {
        return user != null;
    }

    // FIXME: find a way to do it while keeping compatibility with API level 15.
    @RequiresApi(api = Build.VERSION_CODES.N)
    public Optional<User> getUser() {
        return Optional.ofNullable(user);
    }

    // FIXME: move to another class?
    /**
     * Check the credentials of an user and set local session if successful.
     * @param user user to authenticate against
     * @param password password to check
     * @return true if the login was successful
     */
    public boolean login(User user, String password) {
        if (user.checkPassword(password)) {
            token = "secret";
            setCurrentUser(user);
            return true;
        } else {
            reset();
            return false;
        }
    }

    // FIXME: move to another class?
    public void logout() {
        reset();
    }
}
