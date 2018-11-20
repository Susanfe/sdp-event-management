package ch.epfl.sweng.eventmanager.users;

import java.util.Arrays;

/**
 * Defines the clearance levels for authenticated users accessing an event.
 */
public enum Role {
    ADMIN,
    STAFF;

    public static String[] asArrayOfString() {
        String rawValues = Arrays.toString(Role.values());
        return rawValues.substring(1, rawValues.length()-1).replace(" ", "").split(",");
    }
}
