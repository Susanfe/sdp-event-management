package ch.epfl.sweng.eventmanager.repository;

import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;
import ch.epfl.sweng.eventmanager.repository.room.daos.GenericDAO;

import java.util.List;

/**
 * @author Louis Vialar
 */
public abstract class AbstractRoomRepository<T, U extends GenericDAO<T>> {
    protected U dao;

    public AbstractRoomRepository(U dao) {
        this.dao = dao;
    }

    public LiveData<List<T>> findAll(){
        return dao.getAll();
    }

    public void insert(T joinedEvent){
        new InsertAsyncTask<>(dao).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR , joinedEvent);
    }

    public void delete(T joinedEvent){
        new DeleteAsyncTask<>(dao).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, joinedEvent);
    }

    /**
     * Defines adding to the database with an asynchronous task
     * @see android.os.AsyncTask
     */
    private static class InsertAsyncTask<T, U extends GenericDAO<T>> extends AsyncTask<T, Void, Void> {

        private U mAsyncTaskDao;

        InsertAsyncTask(U dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final T... joinedEvents) {
            mAsyncTaskDao.insert(joinedEvents);
            return null;
        }
    }

    /**
     * Defines deleting a JoinedEvent from the database with an asynchronous task
     * @see android.os.AsyncTask
     */
    private static class DeleteAsyncTask<T, U extends GenericDAO<T>> extends AsyncTask<T, Void, Void> {

        private U mAsyncTaskDao;

        DeleteAsyncTask(U dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final T... joinedEvents) {
            mAsyncTaskDao.delete(joinedEvents);
            return null;
        }
    }
}
