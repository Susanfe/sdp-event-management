package ch.epfl.sweng.eventmanager.ui.eventShowcase.models;

import android.arch.lifecycle.*;

import ch.epfl.sweng.eventmanager.repository.EventRepository;
import ch.epfl.sweng.eventmanager.repository.data.Event;
import ch.epfl.sweng.eventmanager.repository.JoinedEventRepository;
import ch.epfl.sweng.eventmanager.repository.data.JoinedEvent;
import ch.epfl.sweng.eventmanager.repository.data.EventLocation;

import javax.inject.Inject;

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

    public LiveData<EventLocation> getLocation() {
        return Transformations.map(getEvent(), ev -> {
            if (ev == null || ev.getLocation() == null) {
                // By default, return EPFL
                return new EventLocation("EPFL", 46.518510, 6.563249);
            }

            return ev.getLocation();
        });
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
