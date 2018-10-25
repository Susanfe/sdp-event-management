package ch.epfl.sweng.eventmanager.ui.eventShowcase.models;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import ch.epfl.sweng.eventmanager.repository.JoinedScheduleItemRepository;
import ch.epfl.sweng.eventmanager.repository.ScheduledItemRepository;
import ch.epfl.sweng.eventmanager.repository.SpotsRepository;
import ch.epfl.sweng.eventmanager.repository.data.JoinedScheduleItem;
import ch.epfl.sweng.eventmanager.repository.data.ScheduledItem;
import ch.epfl.sweng.eventmanager.repository.data.Spot;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * This is the model for the concert list. It connects with the repository to pull a list of scheduledItems and communicate them
 * to the view (here, the activity).
 *
 * @author Louis Vialar
 */
public class SpotsModel extends ViewModel {
    private LiveData<List<Spot>> spots;

    private SpotsRepository repository;

    private int eventId;

    @Inject
    public SpotsModel(SpotsRepository repository) {
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
