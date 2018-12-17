package ch.epfl.sweng.eventmanager.repository.room;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import ch.epfl.sweng.eventmanager.repository.data.JoinedEvent;
import ch.epfl.sweng.eventmanager.repository.data.JoinedScheduleItem;
import ch.epfl.sweng.eventmanager.repository.room.daos.JoinedEventDao;
import ch.epfl.sweng.eventmanager.repository.room.daos.JoinedScheduleItemDao;

/**
 * Defines the database for storing joined events.
 * @see androidx.room.RoomDatabase
 * More info {https://codelabs.developers.google.com/codelabs/android-room-with-a-view/#0}
 */
@Database(entities = {JoinedEvent.class, JoinedScheduleItem.class}, version = 1, exportSchema = false)
public abstract class AppDataBase extends RoomDatabase {
    public abstract JoinedEventDao getJoinedEventDao();

    public abstract JoinedScheduleItemDao getJoinedScheduleItemDao();
}
