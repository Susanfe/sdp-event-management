package ch.epfl.sweng.eventmanager.repository;

import androidx.lifecycle.LiveData;
import ch.epfl.sweng.eventmanager.repository.room.daos.GenericEventDAO;

import java.util.List;

/**
 * @author Louis Vialar
 */
public abstract class AbstractEventRepository<T, U extends GenericEventDAO<T, IdType>, IdType> extends AbstractRoomRepository<T, U> {
    AbstractEventRepository(U dao) {
        super(dao);
    }

    public LiveData<List<IdType>> findAllIds(){
        return dao.getAllIds();
    }

    public LiveData<T> findById(IdType eventId) {
        return dao.findById(eventId);
    }
}
