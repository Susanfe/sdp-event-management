package ch.epfl.sweng.eventmanager.repository.room;

import android.arch.persistence.room.TypeConverter;

import java.util.UUID;

/**
 * @author Louis Vialar
 */
public class Converters {


    @TypeConverter
    public static UUID fromString(String value) {
        return UUID.fromString(value);
    }

    @TypeConverter
    public static String toString(UUID value) {
        return value.toString();
    }

}
