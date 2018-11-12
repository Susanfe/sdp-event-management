package ch.epfl.sweng.eventmanager.repository;

import android.arch.lifecycle.LiveData;
import ch.epfl.sweng.eventmanager.repository.data.News;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import javax.inject.Singleton;
import java.util.List;

/**
 * @author Louis Vialar
 */
@Singleton
public class FirebaseNewsRepository implements NewsRepository {
    private static final String TAG = "FirebaseNewsRepository";

    public FirebaseNewsRepository() {
    }

    @Override
    public Task<Void> publishNews(int eventId, News news) {
        DatabaseReference dbRef = FirebaseDatabase.getInstance()
                .getReference("news")
                .child("event_" + eventId)
                .push(); // Create a new key in the list

        return dbRef.setValue(news);
    }

    @Override
    public LiveData<List<News>> getNews(int eventId) {
        DatabaseReference dbRef = FirebaseDatabase.getInstance()
                .getReference("news")
                .child("event_" + eventId);

        return FirebaseHelper.getList(dbRef, News.class);
    }
}
