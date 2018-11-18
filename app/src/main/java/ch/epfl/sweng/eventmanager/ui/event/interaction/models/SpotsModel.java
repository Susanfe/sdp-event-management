package ch.epfl.sweng.eventmanager.ui.event.interaction.models;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import ch.epfl.sweng.eventmanager.repository.EventRepository;
import ch.epfl.sweng.eventmanager.repository.data.Spot;

import javax.inject.Inject;
import java.util.List;

/**
 * This is the model for the concert list. It connects with the repository to pull a list of scheduledItems and communicate them
 * to the view (here, the activity).
 *
 * @author Louis Vialar
 */
public class SpotsModel extends ViewModel {
    private LiveData<List<Spot>> spots;

    private EventRepository repository;

    private int eventId;

    @Inject
    public SpotsModel(EventRepository repository) {
        this.repository = repository;
    }

    public void init(int eventId) {
        if (this.spots != null) {
            return;
        }

        this.eventId = eventId;
        this.spots = repository.getSpots(eventId);
    }

    public LiveData<List<Spot>> getSpots() {
        return spots;
    }
}
