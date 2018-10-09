package ch.epfl.sweng.eventmanager.repository;

import android.os.Build;
import android.support.annotation.RequiresApi;

import ch.epfl.sweng.eventmanager.repository.data.User;

import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Singleton
public class UserRepository {
    private final List<User> USERS = new ArrayList<>(1);
    private static UserRepository instance = null;

    {
        USERS.add(
               new User(1, "Lamb Da", "lamb.da@domain.tld")
        );
    }

   // Private constructor to disable instantiation.
   private UserRepository() {}

   public static UserRepository getInstance() {
      if(instance == null) {
         instance = new UserRepository();
      }
      return instance;
   }

    public List<User> getUsers() {
        return USERS;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public Optional<User> getUserByEmail(String email) {
        List<User> filtered = USERS.stream()
                .filter(u -> Objects.equals(u.getEmail(), email))
                .collect(Collectors.toList());

        if (filtered.isEmpty()) {
            return Optional.ofNullable(null);
        } else {
            return Optional.ofNullable(filtered.get(0));
        }
    }
}
