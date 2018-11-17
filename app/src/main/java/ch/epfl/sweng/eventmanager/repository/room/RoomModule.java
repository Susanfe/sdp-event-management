package ch.epfl.sweng.eventmanager.repository.room;

import android.app.Application;
import androidx.room.Room;
import ch.epfl.sweng.eventmanager.repository.room.daos.JoinedEventDao;
import ch.epfl.sweng.eventmanager.repository.room.daos.JoinedScheduleItemDao;
import dagger.Module;
import dagger.Provides;

import javax.inject.Singleton;

/**
 * Module for the Room persistence library
 * More info {https://medium.com/@marco_cattaneo/integrate-dagger-2-with-room-persistence-library-in-few-lines-abf48328eaeb}
 */
@Module
public class RoomModule {

    private AppDataBase appDatabase;

    public RoomModule(Application mApplication) {
        appDatabase = Room.databaseBuilder(mApplication, AppDataBase.class, "sdp-event-management").build();
    }

    @Singleton
    @Provides
    AppDataBase providesRoomDatabase() {
        return appDatabase;
    }

    @Singleton
    @Provides
    JoinedEventDao providesProductDao(AppDataBase appDatabase) {
        return appDatabase.getJoinedEventDao();
    }

    @Singleton
    @Provides
    JoinedScheduleItemDao providesMyScheduleDAO(AppDataBase appDatabase) {
        return appDatabase.getJoinedScheduleItemDao();
    }

}
