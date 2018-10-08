package ch.epfl.sweng.eventmanager.ui.eventShowcase;

import android.arch.lifecycle.*;
import android.support.annotation.Nullable;
import android.util.Log;
import ch.epfl.sweng.eventmanager.repository.EventRepository;
import ch.epfl.sweng.eventmanager.repository.data.Event;
import ch.epfl.sweng.eventmanager.room.JoinedEventRepository;
import ch.epfl.sweng.eventmanager.room.data.JoinedEvent;

import javax.inject.Inject;
import java.util.List;

/**
 * This is the model for the event list. It connects with the repository to pull a list of events and communicate them
 * to the view (here, the activity).
 *
 * @author Louis Vialar
 */
public class EventShowcaseModel extends ViewModel {
    private LiveData<Event> event;

    private EventRepository eventRepository;
    private JoinedEventRepository joinedEventRepository;

    @Inject
    public EventShowcaseModel(EventRepository eventRepository, JoinedEventRepository joinedEventRepository) {
        this.eventRepository = eventRepository;
        this.joinedEventRepository = joinedEventRepository;
    }

    public void init(int eventId) {
        if (this.event != null) {
            return;
        }

        this.event = eventRepository.getEvent(eventId);
    }

    public LiveData<Event> getEvent() {
        return event;
    }

    public void joinEvent(Event event){
        joinedEventRepository.insert(new JoinedEvent(event));
    }

    public LiveData<Boolean> isJoined(Event event) {
        return Transformations.map(joinedEventRepository.findById(event.getId()), ev -> ev != null);
    }

    public void unjoinEvent(Event event){
        joinedEventRepository.delete(new JoinedEvent(event));
    }
}
