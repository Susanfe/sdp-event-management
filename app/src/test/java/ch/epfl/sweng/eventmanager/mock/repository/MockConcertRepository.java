package ch.epfl.sweng.eventmanager.mock.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import ch.epfl.sweng.eventmanager.repository.data.ScheduledItem;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class MockConcertRepository {
    private static final String TAG = "ConcertRepository";
    private List<ScheduledItem> concerts = new ArrayList<>();

    public MockConcertRepository() {
        ScheduledItem c1 = new ScheduledItem(new Date(2018, 16, 5), "Michael Jackson", "Pop", "Il est rescussité !", 3, UUID.randomUUID(), "Concert", "Polyv");
        ScheduledItem c2 = new ScheduledItem(new Date(2018, 16, 6), "Daft Punk", "Pop", "Les frenchies en force", 1.75, UUID.randomUUID(), "Concert", "Polyv");
        ScheduledItem c3 = new ScheduledItem(new Date(2018, 16, 7), "Donna Summer", "Disco", "Disco is love", 1.5, UUID.randomUUID(), "Concert", "CO");
        ScheduledItem c4 = new ScheduledItem(new Date(2018, 16, 11), "Booba", "Rap", "Fuck tout le monde", 2.6, UUID.randomUUID(), "Concert", "CE");
        ScheduledItem c5 = new ScheduledItem(new Date(2018, 16, 10), "Black M", "R&B", "Le retour...", 1, UUID.randomUUID(), "Concert", "CE");
        concerts.add(c1);
        concerts.add(c2);
        concerts.add(c3);
        concerts.add(c4);
        concerts.add(c5);

    }

    public LiveData<List<ScheduledItem>> getConcerts(int eventId) {
        final MutableLiveData<List<ScheduledItem>> data = new MutableLiveData<>();
        data.postValue(concerts);

        return data;
    }
}
