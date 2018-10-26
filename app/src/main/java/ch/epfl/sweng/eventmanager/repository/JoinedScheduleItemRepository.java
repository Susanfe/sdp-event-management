package ch.epfl.sweng.eventmanager.repository;

import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;
import ch.epfl.sweng.eventmanager.R;
import ch.epfl.sweng.eventmanager.repository.data.JoinedScheduleItem;
import ch.epfl.sweng.eventmanager.repository.room.daos.JoinedScheduleItemDao;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

@Singleton
public class JoinedScheduleItemRepository extends AbstractEventRepository<JoinedScheduleItem, JoinedScheduleItemDao, UUID> {
    @Inject
    public JoinedScheduleItemRepository(JoinedScheduleItemDao joinedScheduleItemDao){
        super(joinedScheduleItemDao);
    }

    public LiveData<List<JoinedScheduleItem>> findByEventId(int id) {
        return dao.findByEventId(id);
    }

    public AsyncTask insert(JoinedScheduleItem joinedItem){
        return new InsertAsyncTask<>(dao).execute(joinedItem);
    }

    public AsyncTask delete(JoinedScheduleItem joinedItem){
        return new DeleteAsyncTask<>(dao).execute(joinedItem);
    }

    public AsyncTask toggle(JoinedScheduleItem joinedScheduleItem, Context context) {
        return new ToggleTask(dao, context).execute(joinedScheduleItem);
    }

    /**
     * Defines toggling a JoinedScheduleItem from the database with an asynchronous task
     * @see android.os.AsyncTask
     */
    protected static class ToggleTask extends AsyncTask<JoinedScheduleItem, Void, Boolean> {

        private JoinedScheduleItemDao mAsyncTaskDao;
        private final AtomicReference<Context> context = new AtomicReference<>();

        ToggleTask(JoinedScheduleItemDao dao, Context context) {
            mAsyncTaskDao = dao;
            this.context.set(context);
        }

        @Override
        protected Boolean doInBackground(final JoinedScheduleItem... joinedEvents) {
            return (processOne(joinedEvents[0]));
        }

        /**
         * Adds or remove an item from the joined items, depending on whether or not it was joined before
         * @param item the item to add or remove
         * @return true if it was inserted, false if it was removed
         */
        private boolean processOne(JoinedScheduleItem item) {
            JoinedScheduleItem joined = mAsyncTaskDao.findByIdImmediate(item.getUid());

            if (joined == null) {
                mAsyncTaskDao.insert(item);
                return true;
            } else {
                mAsyncTaskDao.delete(item);
                return false;
            }

        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            if (aBoolean) {
                Toast.makeText(context.get(), R.string.timeline_view_added_to_own_schedule, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context.get(), R.string.timeline_view_removed_from_own_schedule, Toast.LENGTH_SHORT).show();
            }
        }
    }
}
