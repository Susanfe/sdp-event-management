package ch.epfl.sweng.eventmanager.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.util.Log;
import ch.epfl.sweng.eventmanager.repository.data.ScheduledItem;
import com.google.firebase.database.*;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;

/**
 * @author Louis Vialar
 */
@Singleton
public class ScheduledItemRepository extends AbstractEventDataRepository<ScheduledItem> {
    private static final String TAG = "ScheduledItemRepository";

    @Inject
    public ScheduledItemRepository() {
        super("schedule_items", new GenericTypeIndicator<List<ScheduledItem>>() {});
    }

    public LiveData<List<ScheduledItem>> getConcerts(int eventId) {
        return this.getElems(eventId);
    }
}
