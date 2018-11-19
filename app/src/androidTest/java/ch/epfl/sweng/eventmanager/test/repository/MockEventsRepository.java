package ch.epfl.sweng.eventmanager.test.repository;

import androidx.lifecycle.LiveData;
import android.graphics.Bitmap;
import ch.epfl.sweng.eventmanager.repository.EventRepository;
import ch.epfl.sweng.eventmanager.repository.data.*;
import ch.epfl.sweng.eventmanager.test.ObservableMap;
import ch.epfl.sweng.eventmanager.test.ticketing.MockStacks;
import ch.epfl.sweng.eventmanager.users.DummyInMemorySession;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.*;

/**
 * @author Louis Vialar
 */
public class MockEventsRepository implements EventRepository {
    public static final Map<Integer, EventTicketingConfiguration> CONFIG_BY_EVENT;

    static {
        Map<Integer, EventTicketingConfiguration> configurationMap = new HashMap<>();
        configurationMap.put(1, MockStacks.BASIC_CONFIGURATION);
        configurationMap.put(2, MockStacks.MULTI_CONFIGURATION);
        configurationMap.put(3, MockStacks.AUTH_MULTI_CONFIGURATION);

        CONFIG_BY_EVENT = Collections.unmodifiableMap(configurationMap);
    }

    private final ObservableMap<Integer, Event> events = new ObservableMap<>();
    private final ObservableMap<Integer, Bitmap> eventImages = new ObservableMap<>();
    private final ObservableMap<Integer, List<Spot>> spots = new ObservableMap<>();
    private final ObservableMap<Integer, List<ScheduledItem>> scheduledItems = new ObservableMap<>();

    {
        EventOrganizer orga = new EventOrganizer(1, "Some Organizer 1", "Orga Description", null, "events@epfl.ch");
        // Init events

        TypeToken<List<Spot>> spotsToken = new TypeToken<List<Spot>>() {
        };

        String jsonSpots = "[ {\n" +
                "  \"position\" : {\n" +
                "    \"latitude\" : 46.517799,\n" +
                "    \"longitude\" : 6.566737\n" +
                "  },\n" +
                "  \"spotType\" : \"SCENE\",\n" +
                "  \"title\" : \"Grande scène\"\n" +
                "}, {\n" +
                "  \"position\" : {\n" +
                "    \"latitude\" : 46.520433,\n" +
                "    \"longitude\" : 6.567822\n" +
                "  },\n" +
                "  \"spotType\" : \"BAR\",\n" +
                "  \"title\" : \"Satellite\"\n" +
                "}, {\n" +
                "  \"position\" : {\n" +
                "    \"latitude\" : 46.523,\n" +
                "    \"longitude\" : 6.567822\n" +
                "  },\n" +
                "  \"spotType\" : \"NURSERY\",\n" +
                "  \"title\" : \"test3\"\n" +
                "}, {\n" +
                "  \"position\" : {\n" +
                "    \"latitude\" : 46.520433,\n" +
                "    \"longitude\" : 6.5679\n" +
                "  },\n" +
                "  \"spotType\" : \"WC\",\n" +
                "  \"title\" : \"test4\"\n" +
                "}, {\n" +
                "  \"position\" : {\n" +
                "    \"latitude\" : 46.520433,\n" +
                "    \"longitude\" : 6.55555\n" +
                "  },\n" +
                "  \"spotType\" : \"INFORMATION\",\n" +
                "  \"title\" : \"test5\"\n" +
                "} ]\n";

        Map<String, List<String>> usersMap = new HashMap<>();
        usersMap.put("admin", Collections.singletonList(DummyInMemorySession.DUMMY_UID));


        addEvent(new Event(1, "Event with scheduled items", "Description", new Date(1550307600L), new Date(1550422800L),
                orga, null, new EventLocation("EPFL", Position.EPFL), new Gson().fromJson(jsonSpots, spotsToken.getType()), usersMap, "JapanImpact",
                CONFIG_BY_EVENT.get(1)));

        addEvent(new Event(2, "Event without items", "Description", new Date(1550307600L), new Date(1550422800L),
                orga, null, new EventLocation("EPFL", Position.EPFL), Collections.emptyList(), usersMap, "JapnImpact",
                CONFIG_BY_EVENT.get(2)));

        addEvent(new Event(3, "Event without items B", "Description", new Date(1550307600L), new Date(1550422800L),
                orga, null, new EventLocation("EPFL", Position.EPFL), Collections.emptyList(), usersMap, "JapanImpact",
                CONFIG_BY_EVENT.get(3)));

        List<ScheduledItem> items;
        String jsonSchedule = "[ {\n" +
                "  \"artist\" : \"David Guetta\",\n" +
                "  \"date\" : 1744913000000,\n" +
                "  \"description\" : \"Incredible stage performance by famous DJ David Guetta!\",\n" +
                "  \"duration\" : 1.5,\n" +
                "  \"genre\" : \"Electro/Dance\",\n" +
                "  \"id\" : \"b52a251a-6ee5-4ef2-af5b-4475a8244aaf\",\n" +
                "  \"itemLocation\" : \"Polyv\"\n" +
                "}, {\n" +
                "  \"artist\" : \"ABBA\",\n" +
                "  \"date\" : 1744904900000,\n" +
                "  \"description\" : \"Wow! This is the great comeback of the well-known success group!\",\n" +
                "  \"duration\" : 2,\n" +
                "  \"genre\" : \"Rock\",\n" +
                "  \"id\" : \"d41e8f29-51db-4924-8363-1bcc1e98631d\",\n" +
                "  \"itemLocation\" : \"CE\"\n" +
                "}, {\n" +
                "  \"artist\" : \"Daft Punk\",\n" +
                "  \"date\" : 1744904900000,\n" +
                "  \"description\" : \"Le retour des frenchies !\",\n" +
                "  \"duration\" : 1.5,\n" +
                "  \"genre\" : \"Pop/House\",\n" +
                "  \"id\" : \"90b8b958-9915-4647-809d-470122c28265\",\n" +
                "  \"itemLocation\" : \"Polyv\"\n" +
                "}, {\n" +
                "  \"artist\" : \"Black M\",\n" +
                "  \"date\" : 1741980110000,\n" +
                "  \"description\" : \"Le retour !\",\n" +
                "  \"genre\" : \"R&B/Pop\",\n" +
                "  \"id\" : \"7a9207df-202b-4e83-93a5-8466563466ca\",\n" +
                "  \"itemLocation\" : \"CO\"\n" +
                "} ]\n";

        TypeToken<List<ScheduledItem>> scheduleToken = new TypeToken<List<ScheduledItem>>() {
        };
        items = new Gson().fromJson(jsonSchedule, scheduleToken.getType());

        this.scheduledItems.put(1, items);
    }

    private void addEvent(Event event) {
        events.put(event.getId(), event);
        spots.put(event.getId(), event.getSpotList());
        eventImages.put(event.getId(), event.getImage());
    }

    @Override
    public LiveData<Collection<Event>> getEvents() {
        return events.values();
    }

    @Override
    public LiveData<Event> getEvent(int eventId) {
        return events.get(eventId);
    }

    @Override
    public LiveData<Bitmap> getEventImage(Event event) {
        return eventImages.get(event.getId());
    }

    @Override
    public LiveData<List<Spot>> getSpots(int eventId) {
        return spots.get(eventId);
    }

    @Override
    public LiveData<List<ScheduledItem>> getScheduledItems(int eventId) {
        return scheduledItems.get(eventId);
    }
}
