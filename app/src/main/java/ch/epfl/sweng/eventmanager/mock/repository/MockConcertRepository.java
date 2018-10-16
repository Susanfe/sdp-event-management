package ch.epfl.sweng.eventmanager.mock.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.util.Log;
import ch.epfl.sweng.eventmanager.repository.ConcertRepository;
import ch.epfl.sweng.eventmanager.repository.data.Concert;
import com.google.firebase.database.*;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Louis Vialar
 */
@Singleton
public class MockConcertRepository {
    private static final String TAG = "ConcertRepository";
    private List<Concert> concerts = new ArrayList<>();

    @Inject
    public MockConcertRepository() {
        Concert c1 = new Concert(new Date(2018,16,5), "Michael Jackson", "Pop","Il est rescussité !", 3);
        Concert c2 = new Concert(new Date(2018,16,6), "Daft Punk", "Pop","Les frenchies en force", 1.75);
        Concert c3 = new Concert(new Date(2018,16,7), "Donna Summer", "Disco","Disco is love", 1.5);
        Concert c4 = new Concert(new Date(2018,16,11), "Booba", "Rap","Fuck tout le monde", 2.6);
        Concert c5 = new Concert(new Date(2018,16,10), "Black M", "R&B","Le retour...", 1);
        concerts.add(c1);
        concerts.add(c2);
        concerts.add(c3);
        concerts.add(c4);
        concerts.add(c5);

    }

    public LiveData<List<Concert>> getConcerts(int eventId) {
        final MutableLiveData<List<Concert>> data = new MutableLiveData<>();
        data.postValue(concerts);

        return data;
    }
}
