package ch.epfl.sweng.eventmanager.room;

import android.arch.lifecycle.LiveData;
import ch.epfl.sweng.eventmanager.room.data.JoinedEvent;
import ch.epfl.sweng.eventmanager.room.data.JoinedEventDao;

import java.util.List;

public class JoinedEventRepository {

    private JoinedEventDao joinedEventDao;

    public JoinedEventRepository(JoinedEventDao joinedEventDao){
        this.joinedEventDao = joinedEventDao;
    }

    public LiveData<List<JoinedEvent>> findAll(){
        return joinedEventDao.getAll();
    }

    public LiveData<JoinedEvent> findById(int eventId){
        return joinedEventDao.findById(eventId);
    }

    public LiveData<JoinedEvent> findByName(String name){
        return joinedEventDao.findByName(name);
    }

    public void insert(JoinedEvent joinedEvent){
        joinedEventDao.insert(joinedEvent);
    }

    public void insertAll(JoinedEvent... joinedEvents){
        joinedEventDao.insertAll(joinedEvents);
    }

    public int delete(JoinedEvent joinedEvent){
        return joinedEventDao.delete(joinedEvent);
    }

    public int deleteAll(JoinedEvent... joinedEvents){
        return joinedEventDao.deleteAll(joinedEvents);
    }
}
