package ch.epfl.sweng.eventmanager.room;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import ch.epfl.sweng.eventmanager.room.data.JoinedEvent;
import ch.epfl.sweng.eventmanager.room.data.JoinedEventDao;

@Database(entities = {JoinedEvent.class}, version = 1)
public abstract class AppDataBase extends RoomDatabase {
    public abstract JoinedEventDao getJoinedEventDao();
}
