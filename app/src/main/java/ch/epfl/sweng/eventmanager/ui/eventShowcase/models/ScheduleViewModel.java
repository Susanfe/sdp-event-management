package ch.epfl.sweng.eventmanager.ui.eventShowcase.models;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import ch.epfl.sweng.eventmanager.repository.JoinedScheduleItemRepository;
import ch.epfl.sweng.eventmanager.repository.ScheduledItemRepository;
import ch.epfl.sweng.eventmanager.repository.data.JoinedScheduleItem;
import ch.epfl.sweng.eventmanager.repository.data.ScheduledItem;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * This is the model for the concert list. It connects with the repository to pull a list of scheduledItems and communicate them
 * to the view (here, the activity).
 *
 * @author Louis Vialar
 */
public class ScheduleViewModel extends ViewModel {
    private LiveData<List<ScheduledItem>> scheduledItems;
    private LiveData<List<JoinedScheduleItem>> joinedConcerts;

    private ScheduledItemRepository repository;
    private JoinedScheduleItemRepository joinedScheduleItemRepository;

    private int eventId;

    @Inject
    public ScheduleViewModel(ScheduledItemRepository repository, JoinedScheduleItemRepository joinedScheduleItemRepository) {
        this.repository = repository;
        this.joinedScheduleItemRepository = joinedScheduleItemRepository;
    }

    public void init(int eventId) {
        if (this.scheduledItems != null) {
            return;
        }

        this.eventId = eventId;
        this.scheduledItems = repository.getConcerts(eventId);
        this.joinedConcerts = joinedScheduleItemRepository.findByEventId(eventId);
    }

    public LiveData<List<ScheduledItem>> getScheduledItems() {
        return scheduledItems;
    }

    public Boolean isItemJoined(UUID itemId) {
        // This method doesn't return a live-data because it's not watched by anything
        // we only need to get a value at some point, and LiveDatas don't suit this model
        List<ScheduledItem> events = getJoinedScheduleItems().getValue();

        for (ScheduledItem i : events)
            if (i.getId().equals(itemId))
                return true;

        return false;
    }

    public void addToMySchedule(UUID concert) {
        joinedScheduleItemRepository.insert(new JoinedScheduleItem(concert, eventId));
    }
    public void removeFromMySchedule(UUID concert) {
        joinedScheduleItemRepository.delete(new JoinedScheduleItem(concert, eventId));
    }

    public LiveData<List<ScheduledItem>> getJoinedScheduleItems() {
        LiveData<List<ScheduledItem>> allItems = getScheduledItems();

        return Transformations.switchMap(allItems, items ->
                Transformations.map(this.joinedConcerts, joinedScheduleItems -> {
                    List<ScheduledItem> joined = new ArrayList<>();

                    for (ScheduledItem scheduledItem : items) {
                        for (JoinedScheduleItem joinedScheduleItem : joinedScheduleItems) {
                            if (joinedScheduleItem.getUid().equals(scheduledItem.getId())) {
                                joined.add(scheduledItem);
                                break;
                            }
                        }
                    }
                    return joined;
                })
        );
    }


}
