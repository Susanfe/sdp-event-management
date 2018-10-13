package ch.epfl.sweng.eventmanager.repository;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Query;
import android.os.AsyncTask;
import ch.epfl.sweng.eventmanager.repository.data.JoinedScheduleActivity;
import ch.epfl.sweng.eventmanager.repository.room.JoinedScheduleActivityDao;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;
import java.util.UUID;

@Singleton
public class JoinedScheduleActivityRepository {

    private JoinedScheduleActivityDao joinedScheduleActivityDao;

    @Inject
    public JoinedScheduleActivityRepository(JoinedScheduleActivityDao joinedScheduleActivityDao){
        this.joinedScheduleActivityDao = joinedScheduleActivityDao;
    }

    public LiveData<List<JoinedScheduleActivity>> findAll(){
        return joinedScheduleActivityDao.getAll();
    }

    public LiveData<List<UUID>> findAllIds(){
        return joinedScheduleActivityDao.getAllIds();
    }

    public LiveData<JoinedScheduleActivity> findById(UUID scheduleActivityId) {
        return joinedScheduleActivityDao.findById(scheduleActivityId);
    }

    public LiveData<List<JoinedScheduleActivity>> findByEventId(int id) {
        return joinedScheduleActivityDao.findByEventId(id);
    }

    public void insert(JoinedScheduleActivity joinedScheduleActivity){
        new JoinedScheduleActivityRepository.InsertAsyncTask(joinedScheduleActivityDao).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR , joinedScheduleActivity);
    }

    public void delete(JoinedScheduleActivity joinedScheduleActivity){
        new JoinedScheduleActivityRepository.DeleteAsyncTask(joinedScheduleActivityDao).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, joinedScheduleActivity);
    }

    /**
     * Defines adding to the database with an asynchronous task
     * @see android.os.AsyncTask
     */
    private static class InsertAsyncTask extends AsyncTask<JoinedScheduleActivity, Void, Void> {

        private JoinedScheduleActivityDao mAsyncTaskDao;

        InsertAsyncTask(JoinedScheduleActivityDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final JoinedScheduleActivity... joinedScheduleActivities) {
            mAsyncTaskDao.insert(joinedScheduleActivities);
            return null;
        }
    }

    /**
     * Defines deleting a Joined scheduled activity from the database with an asynchronous task
     * @see android.os.AsyncTask
     */
    private static class DeleteAsyncTask extends AsyncTask<JoinedScheduleActivity, Void, Void> {

        private JoinedScheduleActivityDao mAsyncTaskDao;

        DeleteAsyncTask(JoinedScheduleActivityDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final JoinedScheduleActivity... joinedScheduleActivities) {
            mAsyncTaskDao.delete(joinedScheduleActivities[0]);
            return null;
        }
    }
}
