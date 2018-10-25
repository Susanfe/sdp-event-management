package ch.epfl.sweng.eventmanager.ui.eventShowcase.models;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import ch.epfl.sweng.eventmanager.repository.EventRepository;
import ch.epfl.sweng.eventmanager.repository.JoinedScheduleItemRepository;
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
    private LiveData<List<ScheduledItem>> joinedItems;

    private EventRepository repository;
    private JoinedScheduleItemRepository joinedScheduleItemRepository;

    private int eventId;

    @Inject
    public ScheduleViewModel(EventRepository repository, JoinedScheduleItemRepository joinedScheduleItemRepository) {
        this.repository = repository;
        this.joinedScheduleItemRepository = joinedScheduleItemRepository;
    }

    public void init(int eventId) {
        if (this.scheduledItems != null) {
            return;
        }

        this.eventId = eventId;
        this.scheduledItems = repository.getConcerts(eventId);
        this.joinedItems = buildJoinedScheduledItemsList(joinedScheduleItemRepository.findByEventId(eventId));
    }

    public LiveData<List<ScheduledItem>> getScheduledItems() {
        return scheduledItems;
    }

    public LiveData<List<ScheduledItem>> getJoinedScheduleItems() {
        return joinedItems;
    }

    public void toggleMySchedule(UUID concert, Context context) {
        joinedScheduleItemRepository.toggle(new JoinedScheduleItem(concert, eventId), context);
    }

    private LiveData<List<ScheduledItem>> buildJoinedScheduledItemsList(LiveData<List<JoinedScheduleItem>> joinedConcerts) {
        LiveData<List<ScheduledItem>> allItems = getScheduledItems();

        return Transformations.switchMap(allItems, items ->
                Transformations.map(joinedConcerts, joinedScheduleItems -> {
                    List<ScheduledItem> joined = new ArrayList<>();

                    if (items == null || joinedScheduleItems == null) {
                        return null;
                    }

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
