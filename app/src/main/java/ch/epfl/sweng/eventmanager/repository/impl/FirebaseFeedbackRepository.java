package ch.epfl.sweng.eventmanager.repository.impl;

import androidx.lifecycle.LiveData;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.*;

public class FirebaseFeedbackRepository {

    public Task<Void> publishRating(int eventId, float rating) {
        return FirebaseHelper.publishElement(eventId, "rating", rating);
    }

    public LiveData<Float> getRating(int eventId) {
        DatabaseReference dbRef = FirebaseDatabase.getInstance()
                .getReference("ratings")
                .child("event_" + eventId);

        return FirebaseHelper.getElement(dbRef, Float.class);
    }
}
