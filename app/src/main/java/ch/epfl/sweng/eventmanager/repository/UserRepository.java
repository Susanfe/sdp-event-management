package ch.epfl.sweng.eventmanager.repository;

import ch.epfl.sweng.eventmanager.repository.data.User;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Singleton
public class UserRepository {
    private final List<User> USERS = new ArrayList<>(1);

    {
        USERS.add(
               new User(1, "Lamb Da", "lamb.da@domain.tld")
        );
    }

    // Private constructor to disable instantiation.
    @Inject
    public UserRepository() {}

    public List<User> getUsers() {
        return USERS;
    }

    public User getUserByEmail(String email) {
        List<User> filtered = USERS.stream()
                .filter(u -> Objects.equals(u.getEmail(), email))
                .collect(Collectors.toList());

        if (filtered.isEmpty()) {
            return null;
        } else {
            return filtered.get(0);
        }
    }
}
