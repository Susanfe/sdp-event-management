package ch.epfl.sweng.eventmanager.mock.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;

import ch.epfl.sweng.eventmanager.repository.data.Spot;
import ch.epfl.sweng.eventmanager.repository.data.SpotType;

public class MockSpotRepository {
    private static final String TAG = "SpotRepository";
    private List<Spot> spots = new ArrayList<>();

    public MockSpotRepository(){
        Spot c1 = new Spot();
        Spot spot1 = new Spot("satelitte", SpotType.BAR, 46.520433, 6.567822, null);
        Spot spot2 = new Spot("information", SpotType.INFORMATION, 46.520533, 6.567822, null);
        Spot spot3 = new Spot("wc", SpotType.WC, 46.520633, 6.567822, null);

        spots.add(spot1);
        spots.add(spot2);
        spots.add(spot3);
    }

    public LiveData<List<Spot>> getSpot(int eventId) {
        final MutableLiveData<List<Spot>> data = new MutableLiveData<>();
        data.postValue(spots);

        return data;
    }
}
