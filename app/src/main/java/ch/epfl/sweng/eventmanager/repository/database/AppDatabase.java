package ch.epfl.sweng.eventmanager.repository.database;

import android.arch.persistence.room.Database;

@Database(entities = {Event.class}, version = 1)
public abstract class AppDatabase {
    public abstract EventDao eventDao();
}
