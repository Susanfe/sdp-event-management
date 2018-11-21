package ch.epfl.sweng.eventmanager.ui.event.selection;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;
import ch.epfl.sweng.eventmanager.repository.EventRepository;
import ch.epfl.sweng.eventmanager.repository.JoinedEventRepository;
import ch.epfl.sweng.eventmanager.repository.data.Event;
import ch.epfl.sweng.eventmanager.repository.data.JoinedEvent;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * This is the model for the event list. It connects with the repository to pull a list of events and communicate them
 * to the view (here, the activity).
 *
 * @author Louis Vialar
 */
public class EventPickingModel extends ViewModel {
    private LiveData<? extends Collection<Event>> events;

    private EventRepository eventRepository;
    private JoinedEventRepository joinedEventRepository;

    @Inject
    public EventPickingModel(EventRepository eventRepository, JoinedEventRepository joinedEventRepository) {
        this.eventRepository = eventRepository;
        this.joinedEventRepository = joinedEventRepository;
    }

    public void init() {
        if (this.events != null) {
            return;
        }

        events = eventRepository.getEvents();
    }

    /**
     * Returns a pair of joined and not joined events
     */
    LiveData<EventsPair> getEventsPair() {
        LiveData<List<Integer>> joinedEvents = joinedEventRepository.findAllIds();

        return Transformations.switchMap(events, events -> {
            // Each time the events change, this transformation is triggered

            return Transformations.map(joinedEvents, set -> {
                // Build a set of all the joined event IDs
                List<Event> joined = new ArrayList<>();
                List<Event> notJoined = new ArrayList<>();

                for (Event ev : events) {
                    if (set.contains(ev.getId())) joined.add(ev);
                    else notJoined.add(ev);
                }

                return new EventsPair(joined, notJoined);
            });
        });
    }

    public static class EventsPair {
        private final List<Event> joinedEvents;
        private final List<Event> otherEvents;

        EventsPair(List<Event> joinedEvents, List<Event> otherEvents) {
            this.joinedEvents = Collections.unmodifiableList(joinedEvents);
            this.otherEvents = Collections.unmodifiableList(otherEvents);
        }

        List<Event> getJoinedEvents() {
            return joinedEvents;
        }

        List<Event> getOtherEvents() {
            return otherEvents;
        }
    }

    public void joinEvent(Event event) {
        joinedEventRepository.insert(new JoinedEvent(event));
    }

    public void unjoinEvent(Event event) {
        joinedEventRepository.delete(new JoinedEvent(event));
    }
}
