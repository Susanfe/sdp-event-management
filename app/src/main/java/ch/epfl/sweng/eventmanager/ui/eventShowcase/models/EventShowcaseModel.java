package ch.epfl.sweng.eventmanager.ui.eventShowcase.models;

import android.arch.lifecycle.*;


import android.content.Context;
import android.graphics.Bitmap;

import ch.epfl.sweng.eventmanager.data.JoinedScheduleItem;
import ch.epfl.sweng.eventmanager.data.ScheduledItem;
import ch.epfl.sweng.eventmanager.repository.EventRepository;
import ch.epfl.sweng.eventmanager.data.Event;
import ch.epfl.sweng.eventmanager.repository.JoinedEventRepository;
import ch.epfl.sweng.eventmanager.data.JoinedEvent;
import ch.epfl.sweng.eventmanager.repository.JoinedScheduleItemRepository;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * This is the model for the event list. It connects with the repository to pull a list of events and communicate them
 * to the view (here, the activity).
 *
 * @author Louis Vialar
 */
public class EventShowcaseModel extends ViewModel {
    private LiveData<Event> event;
    private LiveData<Bitmap> eventImage;
    private LiveData<List<ScheduledItem>> joinedItems;

    private EventRepository eventRepository;
    private JoinedEventRepository joinedEventRepository;
    private JoinedScheduleItemRepository joinedScheduleItemRepository;

    private int eventId;

    @Inject
    public EventShowcaseModel(EventRepository eventRepository, JoinedEventRepository joinedEventRepository, JoinedScheduleItemRepository joinedScheduleItemRepository) {
        this.eventRepository = eventRepository;
        this.joinedEventRepository = joinedEventRepository;
        this.joinedScheduleItemRepository = joinedScheduleItemRepository;
    }

    public void init(int eventId) {
        if (this.event != null) {
            return;
        }

        this.event = eventRepository.getEvent(eventId);
        this.eventImage = Transformations.switchMap(this.event, ev -> eventRepository.getEventImage(ev));
        this.joinedItems = buildJoinedScheduledItemsList(joinedScheduleItemRepository.findByEventId(eventId));
    }

    public LiveData<Event> getEvent() {
        return event;
    }

    public LiveData<Bitmap> getEventImage(){
        return eventImage;

    }

    public void joinEvent(Event event){
        joinedEventRepository.insert(new JoinedEvent(event));
    }

    public LiveData<Boolean> isJoined(Event event) {
        return Transformations.map(joinedEventRepository.findById(event.getId()), ev -> ev != null);
    }

    public LiveData<List<ScheduledItem>> getJoinedScheduleItems() {
        return joinedItems;
    }

    public void unjoinEvent(Event event){
        joinedEventRepository.delete(new JoinedEvent(event));
    }

    private LiveData<List<ScheduledItem>> buildJoinedScheduledItemsList(LiveData<List<JoinedScheduleItem>> joinedItems) {
        LiveData<Event> evData = getEvent();

        return Transformations.switchMap(evData, event ->
                Transformations.map(joinedItems, joinedScheduleItems -> {
                    if (event == null) {
                        return null;
                    }

                    List<ScheduledItem> joined = new ArrayList<>();
                    List<ScheduledItem> items = event.getScheduledItems();

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

    public void toggleMySchedule(UUID scheduledItemId, Context context) {
        joinedScheduleItemRepository.toggle(new JoinedScheduleItem(scheduledItemId, eventId), context);
    }
}
