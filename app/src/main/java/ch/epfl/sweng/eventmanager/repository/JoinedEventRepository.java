package ch.epfl.sweng.eventmanager.repository;

import ch.epfl.sweng.eventmanager.repository.data.JoinedEvent;
import ch.epfl.sweng.eventmanager.repository.room.daos.JoinedEventDao;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class JoinedEventRepository extends AbstractEventRepository<JoinedEvent, JoinedEventDao, Integer> {
    @Inject
<<<<<<< HEAD
    public JoinedEventRepository(JoinedEventDao joinedEventDao) {
        super(joinedEventDao);
=======
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

    public AsyncTask<JoinedEvent, Void, Void> insert(JoinedEvent joinedEvent){
        return new InsertAsyncTask(joinedEventDao).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR , joinedEvent);
    }

    public AsyncTask<JoinedEvent, Void, Void> delete(JoinedEvent joinedEvent){
        return new DeleteAsyncTask(joinedEventDao).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, joinedEvent);
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
>>>>>>> origin/feature/improve-test-coverage
    }
}
