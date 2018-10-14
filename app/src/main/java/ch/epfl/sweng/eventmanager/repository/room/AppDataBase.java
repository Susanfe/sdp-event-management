package ch.epfl.sweng.eventmanager.repository.room;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import ch.epfl.sweng.eventmanager.repository.data.JoinedEvent;
import ch.epfl.sweng.eventmanager.repository.data.JoinedScheduleItem;

/**
 * Defines the database for storing joined events.
 * @see android.arch.persistence.room.RoomDatabase
 * More info {https://codelabs.developers.google.com/codelabs/android-room-with-a-view/#0}
 */
@Database(entities = {JoinedEvent.class, JoinedScheduleItem.class}, version = 1)
@TypeConverters({Converters.class})
public abstract class AppDataBase extends RoomDatabase {
    public abstract JoinedEventDao getJoinedEventDao();

    public abstract JoinedScheduleItemDao getJoinedScheduleItemDao();
}
