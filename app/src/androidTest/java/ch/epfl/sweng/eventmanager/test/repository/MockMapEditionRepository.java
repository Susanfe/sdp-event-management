package ch.epfl.sweng.eventmanager.test.repository;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import ch.epfl.sweng.eventmanager.repository.MapEditionRepository;
import ch.epfl.sweng.eventmanager.repository.data.Spot;
import ch.epfl.sweng.eventmanager.repository.data.Zone;

public class MockMapEditionRepository implements MapEditionRepository {

    private String zonesJson = "[ {\n" +
            "      \"positions\" : [ {\n" +
            "        \"latitude\" : 35.69436,\n" +
            "        \"longitude\" : 139.747854\n" +
            "      }, {\n" +
            "        \"latitude\" : 35.683454,\n" +
            "        \"longitude\" : 139.745333\n" +
            "      }, {\n" +
            "        \"latitude\" : 35.676309,\n" +
            "        \"longitude\" : 139.759351\n" +
            "      }, {\n" +
            "        \"latitude\" : 35.69278523609325,\n" +
            "        \"longitude\" : 139.75525248795748\n" +
            "      } ]\n" +
            "    } ]";

    private TypeToken<List<Zone>> zonesToken = new TypeToken<List<Zone>>() {};
    private List<Zone> zones = new Gson().fromJson(zonesJson, zonesToken.getType());

    private String spotsJson = "[ {\n" +
            "      \"position\" : {\n" +
            "        \"latitude\" : 35.68773,\n" +
            "        \"longitude\" : 139.756204\n" +
            "      },\n" +
            "      \"snippet\" : \"scene\",\n" +
            "      \"spotType\" : \"SCENE\",\n" +
            "      \"title\" : \"Musée du Katana\"\n" +
            "    }, {\n" +
            "      \"position\" : {\n" +
            "        \"latitude\" : 35.685132,\n" +
            "        \"longitude\" : 139.748665\n" +
            "      },\n" +
            "      \"snippet\" : \"bar\",\n" +
            "      \"spotType\" : \"BAR\",\n" +
            "      \"title\" : \"Manga Bar\"\n" +
            "    } ]";

    private TypeToken<List<Spot>> spotsToken = new TypeToken<List<Spot>>() {};
    List<Spot> spots = new Gson().fromJson(spotsJson, spotsToken.getType());

    MockMapEditionRepository() { }

    @Override
    public Task<Zone> updateZones(int eventId, Zone zone) {
        return Tasks.forResult(zone);
    }

    @Override
    public Task<List<Spot>> updateSpots(int eventId, List<Spot> spots) {
        return Tasks.forResult(spots);
    }
}
