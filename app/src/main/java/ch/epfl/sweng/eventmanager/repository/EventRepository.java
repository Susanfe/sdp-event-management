package ch.epfl.sweng.eventmanager.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import ch.epfl.sweng.eventmanager.repository.data.Event;
import ch.epfl.sweng.eventmanager.repository.data.EventOrganizer;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Louis Vialar
 */
@Singleton
public class EventRepository {
    // In the future, the events data will come from Firebase
    // From now, we just get them from a static list
    private final List<Event> EVENTS = new ArrayList<>(3);

    {
        EVENTS.add(
                new Event(1, "Japan Impact", "La plus grande convention sur la culture japonaise " +
                        "de Suisse Romande !",
                        new EventOrganizer(1, "PolyJapan", "La commission de l'AGEPoly qui promeut la culture japonaise sur le campus et ses environs", null),
                        null));
        EVENTS.add(
                new Event(2, "Sysmic", "Le festival de musique de l'association des étudiants de Microtechnique",
                        new EventOrganizer(2, "Sysmic", "L'association des étudiants de Microtechnique", null),
                        null));
        EVENTS.add(
                new Event(3, "Souper de section", "Retrouve tes professeurs et camarades lors du souper de section",
                        new EventOrganizer(3, "CLIC", "L'association des étudiants en IC", null),
                        null));
    }

    @Inject
    public EventRepository() {}

    public LiveData<List<Event>> getEvents() {
        final MutableLiveData<List<Event>> data = new MutableLiveData<>();

        data.setValue(Collections.unmodifiableList(EVENTS));

        return data;
    }
}
