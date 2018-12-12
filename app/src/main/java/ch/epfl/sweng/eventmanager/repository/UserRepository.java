package ch.epfl.sweng.eventmanager.repository;

import androidx.lifecycle.LiveData;
import ch.epfl.sweng.eventmanager.repository.data.User;

public interface UserRepository {
    LiveData<User> getUser(String uid);
}
