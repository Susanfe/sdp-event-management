package ch.epfl.sweng.eventmanager.room;

import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;
import android.util.Log;
import ch.epfl.sweng.eventmanager.room.data.JoinedEvent;
import ch.epfl.sweng.eventmanager.room.data.JoinedEventDao;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;

@Singleton
public class JoinedEventRepository {

    private JoinedEventDao joinedEventDao;

    @Inject
    public JoinedEventRepository(JoinedEventDao joinedEventDao){
        this.joinedEventDao = joinedEventDao;
    }

    public LiveData<List<JoinedEvent>> findAll(){
        return joinedEventDao.getAll();
    }

    public LiveData<List<Integer>> findAllIds(){
        return joinedEventDao.getAllIds();
    }

    public LiveData<JoinedEvent> findById(int eventId) {
        return joinedEventDao.findById(eventId);
    }

    public LiveData<JoinedEvent> findByName(String name){
        return joinedEventDao.findByName(name);
    }

    public void insert(JoinedEvent joinedEvent){
        new InsertAsyncTask(joinedEventDao).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR , joinedEvent);
    }

    public void delete(JoinedEvent joinedEvent){
        new DeleteAsyncTask(joinedEventDao).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, joinedEvent);
    }

    /**
     * Defines adding to the database with an asynchronous task
     * @see android.os.AsyncTask
     */
    private static class InsertAsyncTask extends AsyncTask<JoinedEvent, Void, Void> {

        private JoinedEventDao mAsyncTaskDao;

        InsertAsyncTask(JoinedEventDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final JoinedEvent... joinedEvents) {
            mAsyncTaskDao.insert(joinedEvents);
            return null;
        }
    }

    /**
     * Defines deleting a JoinedEvent from the database with an asynchronous task
     * @see android.os.AsyncTask
     */
    private static class DeleteAsyncTask extends AsyncTask<JoinedEvent, Void, Void> {

        private JoinedEventDao mAsyncTaskDao;

        DeleteAsyncTask(JoinedEventDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final JoinedEvent... joinedEvents) {
            mAsyncTaskDao.delete(joinedEvents[0]);
            return null;
        }
    }
}
