package ch.epfl.sweng.eventmanager.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.util.Log;
import ch.epfl.sweng.eventmanager.repository.data.News;
import com.google.firebase.database.*;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Louis Vialar
 */
@Singleton
public class NewsRepository {
    private static final String TAG = "NewsRepository";

    @Inject
    public NewsRepository() {
    }

    public LiveData<List<News>> getNews(int eventId) {
        final MutableLiveData<List<News>> data = new MutableLiveData<>();

        DatabaseReference dbRef = FirebaseDatabase.getInstance()
                .getReference("news")
                .child("event_" + eventId);

        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<News> news = new ArrayList<>();

                for (DataSnapshot child : dataSnapshot.getChildren())
                    news.add(child.getValue(News.class));

                data.postValue(news);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w(TAG, "Error when getting news for event " + eventId, databaseError.toException());
            }
        });

        return data;
    }
}
