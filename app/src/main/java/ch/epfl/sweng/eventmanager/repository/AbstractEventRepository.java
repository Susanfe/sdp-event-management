package ch.epfl.sweng.eventmanager.repository;

import android.arch.lifecycle.LiveData;
import ch.epfl.sweng.eventmanager.repository.data.JoinedEvent;
import ch.epfl.sweng.eventmanager.repository.room.GenericDAO;
import ch.epfl.sweng.eventmanager.repository.room.GenericEventDAO;

import java.util.List;

/**
 * @author Louis Vialar
 */
public abstract class AbstractEventRepository<T, U extends GenericEventDAO<T, IdType>, IdType> extends AbstractRoomRepository<T, U> {
    public AbstractEventRepository(U dao) {
        super(dao);
    }

    public LiveData<List<IdType>> findAllIds(){
        return dao.getAllIds();
    }

    public LiveData<T> findById(IdType eventId) {
        return dao.findById(eventId);
    }

    public LiveData<T> findByName(String name){
        return dao.findByName(name);
    }
}
