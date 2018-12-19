package ch.epfl.sweng.eventmanager.repository;

import androidx.lifecycle.LiveData;

import android.os.AsyncTask;

import ch.epfl.sweng.eventmanager.repository.data.JoinedScheduleItem;
import ch.epfl.sweng.eventmanager.repository.room.daos.JoinedScheduleItemDao;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;
import java.util.UUID;

@Singleton
public class JoinedScheduleItemRepository extends AbstractEventRepository<JoinedScheduleItem, JoinedScheduleItemDao, String> {
    @Inject
    public JoinedScheduleItemRepository(JoinedScheduleItemDao joinedScheduleItemDao) {
        super(joinedScheduleItemDao);
    }

    public LiveData<List<JoinedScheduleItem>> findByEventId(int id) {
        return dao.findByEventId(id);
    }

    public AsyncTask insert(JoinedScheduleItem joinedItem) {
        return new InsertAsyncTask<>(dao).execute(joinedItem);
    }

    public AsyncTask delete(JoinedScheduleItem joinedItem) {
        return new DeleteAsyncTask<>(dao).execute(joinedItem);
    }

    public AsyncTask toggle(JoinedScheduleItem joinedScheduleItem) {
        return toggle(joinedScheduleItem, null);
    }

    public AsyncTask toggle(JoinedScheduleItem joinedScheduleItem, ToggleCallback wasAdded) {
        return new ToggleTask(dao, wasAdded).execute(joinedScheduleItem);
    }

    /**
     * Defines toggling a JoinedScheduleItem from the database with an asynchronous task
     *
     * @see android.os.AsyncTask
     */
    protected static class ToggleTask extends AsyncTask<JoinedScheduleItem, Void, Boolean> {

        private JoinedScheduleItemDao mAsyncTaskDao;
        private final ToggleCallback callback;

        ToggleTask(JoinedScheduleItemDao dao, ToggleCallback callback) {
            mAsyncTaskDao = dao;
            this.callback = callback;
        }

        @Override
        protected Boolean doInBackground(final JoinedScheduleItem... joinedEvents) {
            return (processOne(joinedEvents[0]));
        }

        /**
         * Adds or remove an item from the joined items, depending on whether or not it was joined before
         *
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
            // TODO: we might need to synchronize on UI thread ici (dunno how to do it but probably easy)

            if (this.callback != null) {
                this.callback.onToggle(aBoolean);
            }
        }
    }

    public interface ToggleCallback {
        /**
         * Called when an item is successfully toggled from the mySchedule
         *
         * @param wasAdded true if the item was added, false if it was removed
         */
        void onToggle(boolean wasAdded);
    }
}
