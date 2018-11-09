package ch.epfl.sweng.eventmanager.userManagement;

import ch.epfl.sweng.eventmanager.repository.data.User;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;

import static org.junit.Assert.*;

/**
 * @author Louis Vialar
 */
public class SessionTest {
    private final User user = new User(1, "user1", "user1@users.local");
    private final User userWithPerms = new User(2, "user1", "user1@users.local", new HashSet<>(Arrays.asList(User.Permission.values())));

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

    @Test
    public void permissionsTest() throws Exception {
        InMemorySession.getInstance().reset(); // reset to initial

        for (User.Permission permissions : User.Permission.values()) {
            assertFalse(Session.hasPermission(permissions));
        }

        assertTrue(Session.login(user, "secret"));

        for (User.Permission permissions : User.Permission.values()) {
            assertFalse(Session.hasPermission(permissions));
        }

        Session.logout();
        assertTrue(Session.login(userWithPerms, "secret"));

        for (User.Permission permissions : User.Permission.values()) {
            assertTrue(Session.hasPermission(permissions));
        }

        Session.logout();

        for (User.Permission permissions : User.Permission.values()) {
            assertFalse(Session.hasPermission(permissions));
        }
    }
}