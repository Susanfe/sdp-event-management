package ch.epfl.sweng.eventmanager.repository.impl;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import ch.epfl.sweng.eventmanager.repository.FeedbackRepository;
import ch.epfl.sweng.eventmanager.repository.data.EventRating;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.*;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;

@Singleton
public class FirebaseFeedbackRepository implements FeedbackRepository {
    private static final String FIREBASE_REF = "ratings";

    @Inject
    public FirebaseFeedbackRepository() {
    }

    @Override
    public Task<Void> publishRating(int eventId, EventRating rating) {
        return FirebaseHelper.publishElement(eventId, FIREBASE_REF, rating);
    }

    @Override
    public LiveData<List<EventRating>> getRatings(int eventId) {
        DatabaseReference dbRef = FirebaseDatabase.getInstance()
                .getReference(FIREBASE_REF)
                .child("event_" + eventId);

        return FirebaseHelper.getList(dbRef, EventRating.class);
    }

    @Override
    public LiveData<Float> getTotalRating(int eventId) {
        return Transformations.map(getRatings(eventId), this::getTotalRating);
    }

    private Float getTotalRating(List<EventRating> ratings) {
        float mean = 0;

        for (EventRating eventRating : ratings)
            mean += eventRating.getRating();

        return mean / ratings.size();
    }

    @Override
    public LiveData<Boolean> isRatingAlreadyPublished(int eventId, String deviceId) {
        LiveData<List<EventRating>> eventRatings = getRatings(eventId);

        return Transformations.map(eventRatings, ratings -> {
            for (EventRating eventRating: ratings){
                if (eventRating.getDeviceId().equals(deviceId))
                    return true;
            }
            return false;
        });
    }
}
