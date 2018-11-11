package ch.epfl.sweng.eventmanager.ui.event.selection;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import ch.epfl.sweng.eventmanager.repository.EventRepository;
import ch.epfl.sweng.eventmanager.repository.data.Event;
import ch.epfl.sweng.eventmanager.repository.JoinedEventRepository;

import javax.inject.Inject;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

/**
 * This is the model for the event list. It connects with the repository to pull a list of events and communicate them
 * to the view (here, the activity).
 *
 * @author Louis Vialar
 */
public class EventPickingModel extends ViewModel {
    private LiveData<List<Event>> events;

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
    public LiveData<EventsPair> getEventsPair() {
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

        public EventsPair(List<Event> joinedEvents, List<Event> otherEvents) {
            this.joinedEvents = Collections.unmodifiableList(joinedEvents);
            this.otherEvents = Collections.unmodifiableList(otherEvents);
        }

        public List<Event> getJoinedEvents() {
            return joinedEvents;
        }

        public List<Event> getOtherEvents() {
            return otherEvents;
        }
    }
}
