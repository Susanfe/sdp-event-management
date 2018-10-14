package ch.epfl.sweng.eventmanager.repository;

import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;
import ch.epfl.sweng.eventmanager.repository.data.JoinedScheduleItem;
import ch.epfl.sweng.eventmanager.repository.room.JoinedScheduleItemDao;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;
import java.util.UUID;

@Singleton
public class JoinedScheduleItemRepository {

    private JoinedScheduleItemDao joinedScheduleItemDao;

    @Inject
    public JoinedScheduleItemRepository(JoinedScheduleItemDao joinedScheduleItemDao){
        this.joinedScheduleItemDao = joinedScheduleItemDao;
    }

    public LiveData<List<JoinedScheduleItem>> findAll(){
        return joinedScheduleItemDao.getAll();
    }

    public LiveData<List<UUID>> findAllIds(){
        return joinedScheduleItemDao.getAllIds();
    }

    public LiveData<JoinedScheduleItem> findById(UUID scheduleItemUUID) {
        return joinedScheduleItemDao.findById(scheduleItemUUID);
    }

    public LiveData<List<JoinedScheduleItem>> findByEventId(int id) {
        return joinedScheduleItemDao.findByEventId(id);
    }

    public void insert(JoinedScheduleItem joinedScheduleItem){
        new JoinedScheduleItemRepository.InsertAsyncTask(joinedScheduleItemDao).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR , joinedScheduleItem);
    }

    public void delete(JoinedScheduleItem joinedScheduleItem){
        new JoinedScheduleItemRepository.DeleteAsyncTask(joinedScheduleItemDao).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, joinedScheduleItem);
    }

    /**
     * Defines adding to the database with an asynchronous task
     * @see android.os.AsyncTask
     */
    private static class InsertAsyncTask extends AsyncTask<JoinedScheduleItem, Void, Void> {

        private JoinedScheduleItemDao mAsyncTaskDao;

        InsertAsyncTask(JoinedScheduleItemDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final JoinedScheduleItem... joinedScheduleItems) {
            mAsyncTaskDao.insert(joinedScheduleItems);
            return null;
        }
    }

    /**
     * Defines deleting a Joined scheduled activity from the database with an asynchronous task
     * @see android.os.AsyncTask
     */
    private static class DeleteAsyncTask extends AsyncTask<JoinedScheduleItem, Void, Void> {

        private JoinedScheduleItemDao mAsyncTaskDao;

        DeleteAsyncTask(JoinedScheduleItemDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final JoinedScheduleItem... joinedScheduleItems) {
            mAsyncTaskDao.delete(joinedScheduleItems[0]);
            return null;
        }
    }
}
