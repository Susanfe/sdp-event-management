package ch.epfl.sweng.eventmanager.repository.room.daos;

import androidx.lifecycle.LiveData;

import java.util.List;

/**
 * @author Louis Vialar
 */
public interface GenericDAO<T> {
    void insert(T... elems);

    int delete(T... elems);

    LiveData<List<T>> getAll();
}
