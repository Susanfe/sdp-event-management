package ch.epfl.sweng.eventmanager.test.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import ch.epfl.sweng.eventmanager.repository.FeedbackRepository;
import ch.epfl.sweng.eventmanager.repository.data.EventRating;
import ch.epfl.sweng.eventmanager.test.ObservableList;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MockFeedbackRepository implements FeedbackRepository {
    private Map<Integer, ObservableList<EventRating>> ratings = new HashMap<>();

    private ObservableList<EventRating> getOrCreateRating(int eventId) {
        if (!ratings.containsKey(eventId)) {
            ratings.put(eventId, new ObservableList<>());
        }

        return ratings.get(eventId);
    }

    @Override
    public Task<Void> publishRating(int eventId, EventRating eventRating) {
        getOrCreateRating(eventId).add(eventRating);

        return Tasks.call(() -> null);
    }

    @Override
    public LiveData<List<EventRating>> getRatings(int eventId) {
        return getOrCreateRating(eventId);
    }

    @Override
    public LiveData<Boolean> ratingFromDeviceExists(int eventId, String deviceId) {
        List<EventRating> eventRatings = getOrCreateRating(eventId).getUnderlyingList();
        MutableLiveData<Boolean> value = new MutableLiveData<>();

        for (EventRating eventRating : eventRatings)
            if (eventRating.getDeviceId().equals(deviceId))
                value.setValue(true);
            else value.setValue(false);

        return value;
    }

    @Override
    public LiveData<Float> getMeanRating(int eventId) {
        MutableLiveData<Float> value = new MutableLiveData<>();

        value.setValue(getTotalRating(getOrCreateRating(eventId).getUnderlyingList()));

        return value;
    }

    private Float getTotalRating(List<EventRating> ratings) {
        float mean = 0;

        for (EventRating eventRating : ratings)
            mean += eventRating.getRating();

        return mean / ratings.size();
    }
}
