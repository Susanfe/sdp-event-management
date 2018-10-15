package ch.epfl.sweng.eventmanager.ui.schedule;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import ch.epfl.sweng.eventmanager.repository.JoinedScheduleItemRepository;
import ch.epfl.sweng.eventmanager.repository.ScheduledItemRepository;
import ch.epfl.sweng.eventmanager.repository.data.JoinedScheduleItem;
import ch.epfl.sweng.eventmanager.repository.data.ScheduledItem;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * This is the model for the concert list. It connects with the repository to pull a list of concerts and communicate them
 * to the view (here, the activity).
 *
 * @author Louis Vialar
 */
public class ScheduleViewModel extends ViewModel {
    private LiveData<List<ScheduledItem>> concerts;
    private LiveData<List<JoinedScheduleItem>> joinedConcerts;

    private ScheduledItemRepository repository;
    private JoinedScheduleItemRepository joinedScheduleItemRepository;

    private int eventId;

    @Inject
    public ScheduleViewModel(ScheduledItemRepository repository, JoinedScheduleItemRepository joinedScheduleItemRepository) {
        this.repository = repository;
        this.joinedScheduleItemRepository = joinedScheduleItemRepository;
    }

    public void init(int eventId) {
        if (this.concerts != null) {
            return;
        }

        this.eventId = eventId;
        this.concerts = repository.getConcerts(eventId);
        this.joinedConcerts = joinedScheduleItemRepository.findByEventId(eventId);
    }

    public LiveData<List<ScheduledItem>> getConcerts() {
        return concerts;
    }

    public LiveData<Boolean> isConcertJoined(UUID concert) {
        return Transformations.map(getConcerts(), list -> {
            for (ScheduledItem i : list)
                if (i.getId().equals(concert))
                    return true;

            return false;
        });
    }

    public void addToMySchedule(UUID concert) {
        joinedScheduleItemRepository.insert(new JoinedScheduleItem(concert, eventId));
    }
    public void removeFromMySchedule(UUID concert) {
        joinedScheduleItemRepository.delete(new JoinedScheduleItem(concert, eventId));
    }

    public LiveData<List<ScheduledItem>> getAddedConcerts() {
        LiveData<List<ScheduledItem>> allItems = getConcerts();

        return Transformations.switchMap(allItems, items ->
                Transformations.map(this.joinedConcerts, joinedScheduleItems -> {
                    List<ScheduledItem> joined = new ArrayList<>();

                    for (ScheduledItem scheduledItem : items) {
                        for (JoinedScheduleItem joinedScheduleItem : joinedScheduleItems) {
                            if (joinedScheduleItem.getUid().equals(scheduledItem.getId())) {
                                joined.add(scheduledItem);
                                break;
                            }
                        }
                    }
                    return joined;
                })
        );
    }


}
