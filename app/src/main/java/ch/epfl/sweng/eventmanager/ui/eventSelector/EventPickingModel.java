package ch.epfl.sweng.eventmanager.ui.eventSelector;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import ch.epfl.sweng.eventmanager.repository.EventRepository;
import ch.epfl.sweng.eventmanager.repository.data.Event;
import dagger.Binds;

import javax.inject.Inject;

import java.util.List;

/**
 * This is the model for the event list. It connects with the repository to pull a list of events and communicate them
 * to the view (here, the activity).
 *
 * @author Louis Vialar
 */
public class EventPickingModel extends ViewModel {
    private LiveData<List<Event>> events;

    private EventRepository eventRepository;

    @Inject
    public EventPickingModel(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public void init() {
        if (this.events != null) {
            return;
        }

        events = eventRepository.getEvents();
    }

    public LiveData<List<Event>> getEvents() {
        return events;
    }
}
