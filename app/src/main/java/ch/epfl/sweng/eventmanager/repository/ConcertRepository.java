package ch.epfl.sweng.eventmanager.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.util.Log;
import ch.epfl.sweng.eventmanager.repository.data.Concert;
import ch.epfl.sweng.eventmanager.repository.data.Event;
import com.google.firebase.database.*;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Louis Vialar
 */
@Singleton
public class ConcertRepository {
    private static String TAG = "ConcertRepository";

    private List<Concert> CONCERTS;

    {
        List<Concert> concertsList = new LinkedList<>();
        // Above is temporary

        // Temp fake for visual example
        concertsList.add(new Concert(new Date(2018, 11, 15, 23, 30),
                "David Guetta", "Electro/ Dance",
                "Incredible stage performance by famous DJ David Guetta!", 1.5));
        concertsList.add(new Concert(new Date(2018, 11, 15, 21, 15),
                "ABBA", "Rock",
                "Wow! This is the great comeback of the well-known success group!", 2));

        CONCERTS = Collections.unmodifiableList(concertsList);
    }

    @Inject
    public ConcertRepository() {
    }

    public LiveData<List<Concert>> getConcerts(int eventId) {
        final MutableLiveData<List<Concert>> data = new MutableLiveData<>();

        // TODO: implement proper database sync
        data.setValue(CONCERTS);

        return data;
    }
}
