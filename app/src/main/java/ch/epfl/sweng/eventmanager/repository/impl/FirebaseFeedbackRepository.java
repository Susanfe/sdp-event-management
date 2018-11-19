package ch.epfl.sweng.eventmanager.repository.impl;

import android.view.animation.Transformation;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import ch.epfl.sweng.eventmanager.repository.FeedbackRepository;
import ch.epfl.sweng.eventmanager.repository.data.EventRating;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FirebaseFeedbackRepository implements FeedbackRepository {
    private static final String FIREBASE_REF = "ratings";

    public Task<Void> publishRating(int eventId, EventRating rating) {
        return FirebaseHelper.publishElement(eventId, FIREBASE_REF, rating);
    }

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

        for (EventRating eventRating : ratings) {
            mean += eventRating.getRating();
        }
        mean /= ratings.size();

        return mean;
    }
}
