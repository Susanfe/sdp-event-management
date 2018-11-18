package ch.epfl.sweng.eventmanager.ui.event.interaction.models;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;
import ch.epfl.sweng.eventmanager.repository.EventRepository;
import ch.epfl.sweng.eventmanager.repository.JoinedScheduleItemRepository;
import ch.epfl.sweng.eventmanager.repository.data.JoinedScheduleItem;
import ch.epfl.sweng.eventmanager.repository.data.ScheduledItem;

import javax.inject.Inject;
import java.util.*;

/**
 * This is the model for the scheduled item list. It connects with the repository to pull a list of scheduledItems
 * and communicate them
 * to the view (here, the activity).
 *
 * @author Louis Vialar
 */
public class ScheduleViewModel extends ViewModel {
    private LiveData<List<ScheduledItem>> scheduledItems;
    private LiveData<List<ScheduledItem>> joinedItems;
    private LiveData<Map<String, List<ScheduledItem>>> scheduleItemsByRoom;
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
        this.scheduledItems = repository.getScheduledItems(eventId);
        this.joinedItems = buildJoinedScheduledItemsList(joinedScheduleItemRepository.findByEventId(eventId));
        this.scheduleItemsByRoom = buildScheduleItemByRoom();
    }

    public LiveData<List<ScheduledItem>> getScheduledItems() {
        return scheduledItems;
    }

    public LiveData<List<ScheduledItem>> getScheduleItemsForRoom(String room) {
        return Transformations.map(getScheduleItemsByRoom(), map -> {
            if (map == null) {
                return null;
            } else {
                return map.get(room);
            }
        });
    }

    public LiveData<Map<String, List<ScheduledItem>>> getScheduleItemsByRoom() {
        return scheduleItemsByRoom;
    }

    public LiveData<List<ScheduledItem>> getJoinedScheduleItems() {
        return joinedItems;
    }

    public void toggleMySchedule(UUID scheduledItemId) {
        toggleMySchedule(scheduledItemId, null);
    }

    public void toggleMySchedule(UUID scheduledItemId, JoinedScheduleItemRepository.ToggleCallback wasAdded) {
        joinedScheduleItemRepository.toggle(new JoinedScheduleItem(scheduledItemId, eventId), wasAdded);
    }

    private LiveData<List<ScheduledItem>> buildJoinedScheduledItemsList(LiveData<List<JoinedScheduleItem>> joinedItems) {
        LiveData<List<ScheduledItem>> allItems = getScheduledItems();

        return Transformations.switchMap(allItems, items -> Transformations.map(joinedItems, joinedScheduleItems -> {
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
        }));
    }

    private LiveData<Map<String, List<ScheduledItem>>> buildScheduleItemByRoom() {
        return Transformations.map(getScheduledItems(), concerts -> {
            Map<String, List<ScheduledItem>> scheduleItemsByRoom = new HashMap<>();
            if (concerts == null || concerts.size() <= 0) {
                return null;
            } else {
                for (ScheduledItem scheduledItem : concerts) {
                    if (!scheduleItemsByRoom.containsKey(scheduledItem.getItemLocation())) {
                        List<ScheduledItem> concertList = new ArrayList<>();
                        concertList.add(scheduledItem);
                        scheduleItemsByRoom.put(scheduledItem.getItemLocation(), concertList);
                    } else {
                        scheduleItemsByRoom.get(scheduledItem.getItemLocation()).add(scheduledItem);
                    }
                }
            }
            return scheduleItemsByRoom;
        });
    }
}
