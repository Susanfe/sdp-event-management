package ch.epfl.sweng.eventmanager.users;

import ch.epfl.sweng.eventmanager.test.users.DummyInMemorySession;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ch.epfl.sweng.eventmanager.repository.data.Event;
import ch.epfl.sweng.eventmanager.repository.data.Spot;

public class SessionTest {
    private Session session;

    @Before
    public void disableFirebaseAuth() {
        session = new Session(new DummyInMemorySession());
    }

    @Test
    public void testAuthentication() {
        assert(!session.isLoggedIn());
        session.login(DummyInMemorySession.DUMMY_EMAIL, DummyInMemorySession.DUMMY_PASSWORD, null, null);
        assert(session.isLoggedIn());
        session.logout();
        assert(!session.isLoggedIn());
    }

    @Test
    public void testClearance() {
        // Initialize used structures
        Map<String, Map<String, String>> emptyUserMapping = new HashMap<>();
        Map<String, Map<String, String>> adminUserMapping = new HashMap<>();
        Map<String, Map<String, String>> unknownUserMapping = new HashMap<>();

        HashMap<String, String> adminUids= new HashMap<>();
        adminUids.put("key1", DummyInMemorySession.DUMMY_UID);
        HashMap<String, String> dummyUids = new HashMap<>();
        dummyUids.put("key2", "unknownUid");

        adminUserMapping.put(Role.ADMIN.toString().toLowerCase(), adminUids);
        unknownUserMapping.put(Role.ADMIN.toString().toLowerCase(), dummyUids);

        List<Spot> spotList = new ArrayList<>();
        Event ev1 = new Event(1, "Event 1", "Descr 1", new Date(0), new Date(0),
                null, null, null, emptyUserMapping, null);

        Event ev2 = new Event(2, "Event 2", "Descr 2", new Date(0), new Date(0),
                null, null, null, adminUserMapping, null);

        Event ev3 = new Event(3, "Event 3", "Descr 3", new Date(0), new Date(0),
                null, null, null, unknownUserMapping, null);

        // The user is not logged in, supposed to fail cleanly
        assert(!session.isClearedFor(Role.ADMIN, ev2));

        // User sign in
        session.login(DummyInMemorySession.DUMMY_EMAIL, DummyInMemorySession.DUMMY_PASSWORD,null, null);
        assert(session.isLoggedIn());

        //
        assert(!session.isClearedFor(Role.ADMIN, ev1));
        assert(session.isClearedFor(Role.ADMIN, ev2));
        assert(!session.isClearedFor(Role.STAFF, ev2));
        assert(!session.isClearedFor(Role.ADMIN, ev3));
    }
}