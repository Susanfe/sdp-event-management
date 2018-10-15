package ch.epfl.sweng.eventmanager.repository;

import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;
import ch.epfl.sweng.eventmanager.repository.data.JoinedEvent;
import ch.epfl.sweng.eventmanager.repository.room.JoinedEventDao;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;

@Singleton
public class JoinedEventRepository extends AbstractRoomRepository<JoinedEvent, JoinedEventDao> {
    @Inject
    public JoinedEventRepository(JoinedEventDao joinedEventDao){
        super(joinedEventDao);
    }

    public LiveData<List<Integer>> findAllIds(){
        return dao.getAllIds();
    }

    public LiveData<JoinedEvent> findById(int eventId) {
        return dao.findById(eventId);
    }

    public LiveData<JoinedEvent> findByName(String name){
        return dao.findByName(name);
    }
}
