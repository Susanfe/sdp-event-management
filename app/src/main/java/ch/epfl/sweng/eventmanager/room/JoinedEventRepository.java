package ch.epfl.sweng.eventmanager.room;

import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;
import android.util.Log;
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
        new InsertAsyncTask(joinedEventDao).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR , joinedEvent);
    }

    public void delete(JoinedEvent joinedEvent){
        new DeleteAsyncTask(joinedEventDao).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, joinedEvent);
    }


    private static class InsertAsyncTask extends AsyncTask<JoinedEvent, Void, Void> {

        private JoinedEventDao mAsyncTaskDao;

        InsertAsyncTask(JoinedEventDao dao) {
            mAsyncTaskDao = dao;
        }


        @Override
        protected Void doInBackground(final JoinedEvent... joinedEvents) {
            mAsyncTaskDao.insert(joinedEvents[0]);
            Log.v("Teuteu", "Trying to add event : " + joinedEvents[0].getName());
            return null;
        }
    }

    private static class DeleteAsyncTask extends AsyncTask<JoinedEvent, Void, Void> {

        private JoinedEventDao mAsyncTaskDao;

        DeleteAsyncTask(JoinedEventDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final JoinedEvent... joinedEvents) {
            mAsyncTaskDao.delete(joinedEvents[0]);
            Log.v("Teuteu", "Trying to delete event : " + joinedEvents[0].getName());
            return null;
        }
    }
}
