package ch.epfl.sweng.eventmanager.repository;

import android.arch.persistence.room.Database;
import ch.epfl.sweng.eventmanager.repository.database.Event;
import ch.epfl.sweng.eventmanager.repository.database.EventDao;

@Database(entities = {Event.class}, version = 1)
public abstract class AppDatabase {
    public abstract EventDao eventDao();
}
