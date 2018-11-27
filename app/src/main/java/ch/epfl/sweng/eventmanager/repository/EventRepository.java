package ch.epfl.sweng.eventmanager.repository;

import androidx.lifecycle.LiveData;
import android.graphics.Bitmap;
import ch.epfl.sweng.eventmanager.repository.data.Event;
import ch.epfl.sweng.eventmanager.repository.data.ScheduledItem;
import ch.epfl.sweng.eventmanager.repository.data.Spot;
import ch.epfl.sweng.eventmanager.repository.data.Zone;
import com.google.android.gms.tasks.Task;

import java.util.Collection;
import java.util.List;

/**
 * @author Louis Vialar
 */
public interface EventRepository {
    LiveData<? extends Collection<Event>> getEvents();

    LiveData<Event> getEvent(int eventId);

    LiveData<Bitmap> getEventImage(Event event);

    LiveData<List<Spot>> getSpots(int eventId);

    LiveData<List<ScheduledItem>> getScheduledItems(int eventId);

    LiveData<List<Zone>> getZones(int eventId);

    LiveData<Bitmap> getSpotImage(Spot spot);

    Task<Void> createEvent(Event event);

    /**
     * Updates an event, using the internal eventId of the event to figure out which event to update
     * @param event the event to update
     */
    Task<Void> updateEvent(Event event);
}
