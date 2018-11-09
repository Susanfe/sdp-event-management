package ch.epfl.sweng.eventmanager.repository;

import ch.epfl.sweng.eventmanager.repository.data.User;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.*;
import java.util.stream.Collectors;

@Singleton
public class UserRepository {
    private final List<User> USERS = new ArrayList<>(1);

    {
        USERS.add(
               new User(1, "Lamb Da", "lamb.da@domain.tld")
        );
        USERS.add(
               new User(2, "Admin Admin", "root@root.tld", new HashSet<>(Arrays.asList(User.Permission.values())))
        );
    }

    // Private constructor to disable instantiation.
    @Inject
    public UserRepository() {}

    public List<User> getUsers() {
        return USERS;
    }

    public User getUserByEmail(String email) {
        List<User> filtered = new ArrayList<>();
        for (User user : USERS) {
            if (email.equals((user.getEmail()))) {
                filtered.add(user);
            }
        }

        if (filtered.isEmpty()) {
            return null;
        } else {
            return filtered.get(0);
        }
    }
}
