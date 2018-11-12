package ch.epfl.sweng.eventmanager.test.userManagement;

import android.app.Activity;
import ch.epfl.sweng.eventmanager.repository.data.DummyUser;
import ch.epfl.sweng.eventmanager.repository.data.User;
import ch.epfl.sweng.eventmanager.userManagement.InMemorySession;
import ch.epfl.sweng.eventmanager.userManagement.Session;
import com.google.android.gms.tasks.OnCompleteListener;

import javax.inject.Singleton;
import java.lang.reflect.Field;

/**
 * @author Louis Vialar
 */
@Singleton
public class TestInMemorySession implements InMemorySession {
    public static final TestInMemorySession instance = new TestInMemorySession();
    private User user;

    private TestInMemorySession() {}

    public void setCurrentUser(User user) {
        this.user = user;
    }

    @Override
    public void login(String email, String password, Activity context, OnCompleteListener callback) {
        // do nothing
    }

    public static void enable() {
        try {
            Field f = Session.class.getDeclaredField("session");
            f.setAccessible(true);
            f.set(null, instance); // TADAAA
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new Error(e);
        }
    }

    @Override
    public User getCurrentUser() {
        return user;
    }

    @Override
    public void logout() {
        this.user = null;
    }

    @Override
    public boolean isLoggedIn() {
        return user != null;
    }
}
