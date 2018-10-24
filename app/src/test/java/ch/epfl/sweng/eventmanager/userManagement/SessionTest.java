package ch.epfl.sweng.eventmanager.userManagement;

import ch.epfl.sweng.eventmanager.repository.data.User;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author Louis Vialar
 */
public class SessionTest {
    private final User user = new User(1, "user1", "user1@users.local");

    @Test
    public void loginTest() throws Exception {
        assertFalse(Session.login(user, "wrongPwd"));
        assertFalse(Session.isLoggedIn());

        assertTrue(Session.login(user, "secret"));
        assertTrue(Session.isLoggedIn());

        assertEquals(user, Session.getCurrentUser());
    }

    @Test
    public void logoutTest() throws Exception {
        InMemorySession.getInstance().reset(); // reset to initial

        assertFalse(Session.isLoggedIn());
        assertTrue(Session.login(user, "secret"));

        Session.logout();
        assertFalse(Session.isLoggedIn());
    }
}