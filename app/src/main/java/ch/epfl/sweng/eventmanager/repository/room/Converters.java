package ch.epfl.sweng.eventmanager.repository.room;

import androidx.room.TypeConverter;

import java.util.UUID;

/**
 * @author Louis Vialar
 */
public final class Converters {
    private Converters() {}

    @TypeConverter
    public static UUID fromString(String value) {
        return UUID.fromString(value);
    }

    @TypeConverter
    public static String toString(UUID value) {
        return value.toString();
    }

}
