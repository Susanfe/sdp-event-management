package ch.epfl.sweng.eventmanager.room;

import android.app.Application;
import android.arch.persistence.room.Room;
import android.content.Context;
import ch.epfl.sweng.eventmanager.room.data.JoinedEventDao;
import dagger.Module;
import dagger.Provides;

import javax.inject.Singleton;

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

}
