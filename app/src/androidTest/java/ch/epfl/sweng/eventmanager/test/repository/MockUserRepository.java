package ch.epfl.sweng.eventmanager.test.repository;

import java.util.HashMap;
import java.util.Map;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import ch.epfl.sweng.eventmanager.repository.UserRepository;
import ch.epfl.sweng.eventmanager.repository.data.User;
import ch.epfl.sweng.eventmanager.test.users.DummyInMemorySession;

public class MockUserRepository implements UserRepository {
    private Map<String, User> users;

    public MockUserRepository() {
        users = new HashMap<>();
        User dummyUser = new User(
                DummyInMemorySession.DUMMY_UID,
                DummyInMemorySession.DUMMY_EMAIL,
                DummyInMemorySession.DUMMY_DISPLAYNAME
        );

        users.put(DummyInMemorySession.DUMMY_UID, dummyUser);
    }

    @Override
    public LiveData<User> getUser(String uid) {
        if (users.containsKey(uid)) {
            MutableLiveData<User> output = new MutableLiveData<>();
            output.setValue(users.get(uid));
            return output;
        } else {
            return null;
        }
    }
}
