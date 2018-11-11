package ch.epfl.sweng.eventmanager.ui.event.interaction.models;

import android.arch.lifecycle.*;


import java.io.Serializable;

import android.graphics.Bitmap;

import ch.epfl.sweng.eventmanager.repository.EventRepository;
import ch.epfl.sweng.eventmanager.repository.data.Event;
import ch.epfl.sweng.eventmanager.repository.JoinedEventRepository;
import ch.epfl.sweng.eventmanager.repository.data.JoinedEvent;
import ch.epfl.sweng.eventmanager.repository.data.EventLocation;
import ch.epfl.sweng.eventmanager.repository.data.Position;

import javax.inject.Inject;

/**
 * This is the model for the event list. It connects with the repository to pull a list of events and communicate them
 * to the view (here, the activity).
 *
 * @author Louis Vialar
 */
public class EventInteractionModel extends ViewModel {
    private LiveData<Event> event;
    private LiveData<Bitmap> eventImage;

    private EventRepository eventRepository;
    private JoinedEventRepository joinedEventRepository;


    @Inject
    public EventInteractionModel(EventRepository eventRepository, JoinedEventRepository joinedEventRepository) {
        this.eventRepository = eventRepository;
        this.joinedEventRepository = joinedEventRepository;
    }

    public void init(int eventId) {
        if (this.event != null) {
            return;
        }

        this.event = eventRepository.getEvent(eventId);
        this.eventImage = Transformations.switchMap(this.event, ev -> eventRepository.getEventImage(ev));
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

    public void unjoinEvent(Event event){
        joinedEventRepository.delete(new JoinedEvent(event));
    }
}
