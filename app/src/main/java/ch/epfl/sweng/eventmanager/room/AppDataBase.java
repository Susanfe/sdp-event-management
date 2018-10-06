package ch.epfl.sweng.eventmanager.repository.database;

import android.arch.persistence.room.Database;
import ch.epfl.sweng.eventmanager.room.data.Event;
import ch.epfl.sweng.eventmanager.room.data.EventDao;

@Database(entities = {Event.class}, version = 1)
public abstract class AppDatabase {
    public abstract EventDao eventDao();
}
