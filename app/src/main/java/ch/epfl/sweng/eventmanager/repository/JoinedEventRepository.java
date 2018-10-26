package ch.epfl.sweng.eventmanager.repository;

import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;
import ch.epfl.sweng.eventmanager.data.JoinedEvent;
import ch.epfl.sweng.eventmanager.repository.room.daos.JoinedEventDao;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class JoinedEventRepository extends AbstractEventRepository<JoinedEvent, JoinedEventDao, Integer> {
    @Inject
    public JoinedEventRepository(JoinedEventDao joinedEventDao) {
        super(joinedEventDao);
    }

    public LiveData<JoinedEvent> findByName(String name){
        return dao.findByName(name);
    }

    public AsyncTask insert(JoinedEvent joinedEvent){
        return new InsertAsyncTask<>(dao).execute(joinedEvent);
    }

    public AsyncTask delete(JoinedEvent joinedEvent){
        return new DeleteAsyncTask<>(dao).execute(joinedEvent);
    }
}
