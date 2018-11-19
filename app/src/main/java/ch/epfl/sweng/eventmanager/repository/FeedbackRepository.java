package ch.epfl.sweng.eventmanager.repository;

import androidx.lifecycle.LiveData;
import ch.epfl.sweng.eventmanager.repository.data.EventRating;
import com.google.android.gms.tasks.Task;
import java.util.List;

public interface FeedbackRepository {
    Task<Void> publishRating(int eventId, EventRating eventRating);

    LiveData<List<EventRating>> getRatings(int eventId);

    LiveData<Float> getTotalRating(int eventId);
}
