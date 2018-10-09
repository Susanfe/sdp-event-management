package ch.epfl.sweng.eventmanager.ui.eventShowcase;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import ch.epfl.sweng.eventmanager.repository.EventRepository;
import ch.epfl.sweng.eventmanager.repository.data.Event;
import ch.epfl.sweng.eventmanager.repository.data.EventLocation;

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

    @Inject
    public EventShowcaseModel(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
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
}
