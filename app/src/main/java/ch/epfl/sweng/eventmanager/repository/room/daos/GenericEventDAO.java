package ch.epfl.sweng.eventmanager.repository.room.daos;

import android.arch.lifecycle.LiveData;

import java.util.List;

/**
 * @author Louis Vialar
 */
public interface GenericEventDAO<T, IdType> extends GenericDAO<T> {
    LiveData<List<IdType>> getAllIds();

    LiveData<T> findById(IdType objId);
}
