package ch.epfl.sweng.eventmanager.users;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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