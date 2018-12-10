package ch.epfl.sweng.eventmanager.test.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import ch.epfl.sweng.eventmanager.repository.UserRepository;
import ch.epfl.sweng.eventmanager.repository.data.User;

public class MockUserRepository implements UserRepository {

    @Override
    public LiveData<User> getUser(String uid) {
        return new MutableLiveData<>();
    }
}
