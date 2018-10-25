package ch.epfl.sweng.eventmanager.repository;

import android.arch.lifecycle.LiveData;
import ch.epfl.sweng.eventmanager.repository.data.Spot;
import com.google.firebase.database.GenericTypeIndicator;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;

/**
 * @author Louis Vialar
 */
@Singleton
public class SpotsRepository extends AbstractEventDataRepository<Spot> {
    private static final String TAG = "SpotsRepository";

    @Inject
    public SpotsRepository() {
        super("spots", new GenericTypeIndicator<List<Spot>>() {
        });
    }

    public LiveData<List<Spot>> getSpots(int eventId) {
        return this.getElems(eventId);
    }
}
