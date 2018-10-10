package ch.epfl.sweng.eventmanager.ui.schedule;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import ch.epfl.sweng.eventmanager.repository.ConcertRepository;
import ch.epfl.sweng.eventmanager.repository.EventRepository;
import ch.epfl.sweng.eventmanager.repository.data.Concert;
import ch.epfl.sweng.eventmanager.repository.data.Event;

import javax.inject.Inject;
import java.util.List;

/**
 * This is the model for the concert list. It connects with the repository to pull a list of concerts and communicate them
 * to the view (here, the activity).
 *
 * @author Louis Vialar
 */
public class ScheduleViewModel extends ViewModel {
    private LiveData<List<Concert>> concerts;

    private ConcertRepository repository;

    @Inject
    public ScheduleViewModel(ConcertRepository repository) {
        this.repository = repository;
    }

    public void init(int eventId) {
        if (this.concerts != null) {
            return;
        }

        this.concerts = repository.getConcerts(eventId);
    }

    public LiveData<List<Concert>> getConcerts() {
        return concerts;
    }
}
