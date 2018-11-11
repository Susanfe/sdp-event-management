package ch.epfl.sweng.eventmanager.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.util.Log;
import ch.epfl.sweng.eventmanager.repository.data.Event;
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
        DatabaseReference dbRef = FirebaseDatabase.getInstance()
                .getReference("news")
                .child("event_" + eventId);

        return FirebaseHelper.getList(dbRef, News.class);
    }
}
