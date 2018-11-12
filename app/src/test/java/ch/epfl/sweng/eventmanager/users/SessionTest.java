package ch.epfl.sweng.eventmanager.users;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import ch.epfl.sweng.eventmanager.repository.data.Event;
import ch.epfl.sweng.eventmanager.repository.data.Spot;

public class SessionTest {
    @Before
    public void disableFirebaseAuth() { Session.enforceDummySessions(); }

    @Test
    public void testAuthentication() {
        assert(Session.isLoggedIn() == false);
        Session.login(DummyInMemorySession.DUMMY_EMAIL, DummyInMemorySession.DUMMY_PASSWORD, null, null);
        assert(Session.isLoggedIn() == true);
        Session.logout();
        assert(Session.isLoggedIn() == false);
    }

    @Test
    public void testClearance() {
        // Initialize used structures
        HashMap<String, List<String>> emptyUserMapping = new HashMap<>();
        HashMap<String, List<String>> adminUserMapping = new HashMap<>();
        HashMap<String, List<String>> unknownUserMapping = new HashMap<>();

        adminUserMapping.put(Role.ADMIN.toString().toLowerCase(), Arrays.asList(DummyInMemorySession.DUMMY_UID));
        unknownUserMapping.put(Role.ADMIN.toString().toLowerCase(), Arrays.asList("unknownUid"));

        List<Spot> spotList = new ArrayList<>();
        Event ev1 = new Event(1, "Event 1", "Descr 1", new Date(0), new Date(0),
                null, null, null, spotList, emptyUserMapping, null);

        Event ev2 = new Event(2, "Event 2", "Descr 2", new Date(0), new Date(0),
                null, null, null, spotList, adminUserMapping, null);

        Event ev3 = new Event(3, "Event 3", "Descr 3", new Date(0), new Date(0),
                null, null, null, spotList, unknownUserMapping, null);

        // The user is not logged in, supposed to fail cleanly
        assert(Session.isClearedFor(Role.ADMIN, ev2) == false);

        // User sign in
        Session.login(DummyInMemorySession.DUMMY_EMAIL, DummyInMemorySession.DUMMY_PASSWORD,null, null);
        assert(Session.isLoggedIn() == true);

        //
        assert(Session.isClearedFor(Role.ADMIN, ev1) == false);
        assert(Session.isClearedFor(Role.ADMIN, ev2) == true);
        assert(Session.isClearedFor(Role.STAFF, ev2) == false);
        assert(Session.isClearedFor(Role.ADMIN, ev3) == false);
    }
}