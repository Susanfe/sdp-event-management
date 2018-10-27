package ch.epfl.sweng.eventmanager.ui.eventShowcase.models;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.view.animation.Transformation;
import ch.epfl.sweng.eventmanager.repository.JoinedScheduleItemRepository;
import ch.epfl.sweng.eventmanager.repository.ScheduledItemRepository;
import ch.epfl.sweng.eventmanager.repository.data.JoinedScheduleItem;
import ch.epfl.sweng.eventmanager.repository.data.ScheduledItem;

import javax.inject.Inject;
import java.util.*;

import static java.util.stream.Collectors.groupingBy;

/**
 * This is the model for the concert list. It connects with the repository to pull a list of scheduledItems and communicate them
 * to the view (here, the activity).
 *
 * @author Louis Vialar
 */
public class ScheduleViewModel extends ViewModel {
    private LiveData<List<ScheduledItem>> scheduledItems;
    private LiveData<List<ScheduledItem>> joinedItems;
    private LiveData<Map<String,List<ScheduledItem>>> scheduleItemsByRoom;

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
        this.joinedItems = buildJoinedScheduledItemsList(joinedScheduleItemRepository.findByEventId(eventId));
        this.scheduleItemsByRoom = buildScheduleItemByRoom();
    }

    public LiveData<List<ScheduledItem>> getScheduledItems() {
        return scheduledItems;
    }

    public LiveData<List<ScheduledItem>> getScheduledItemsForRoom(String room) {
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

    public Boolean isItemJoined(UUID itemId) {
        // This method doesn't return a live-data because it's not watched by anything
        // we only need to get a value at some point, and LiveDatas don't suit this model
        List<ScheduledItem> events = getJoinedScheduleItems().getValue();
        if (events != null) {
            for (ScheduledItem i : events)
                if (i.getId().equals(itemId))
                    return true;
        }

        return false;
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

     private LiveData<Map<String,List<ScheduledItem>>> buildScheduleItemByRoom() {
        return Transformations.map(getScheduledItems(), concerts -> {
            Map<String,List<ScheduledItem>> scheduleItemsByRoom = new HashMap<>();
                if(concerts == null || concerts.size() <= 0) {
                    return null;
                } else {
                for (ScheduledItem scheduledItem : concerts) {
                    if (! scheduleItemsByRoom.containsKey(scheduledItem.getItemLocation())) {
                        List<ScheduledItem> concertList = new ArrayList<>();
                        concertList.add(scheduledItem);
                        scheduleItemsByRoom.put(scheduledItem.getItemLocation(),concertList);
                     } else {
                        scheduleItemsByRoom.get(scheduledItem.getItemLocation()).add(scheduledItem);
                    }
                }
            }
            return scheduleItemsByRoom;
        });
    }


}
