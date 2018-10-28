package ch.epfl.sweng.eventmanager.repository.data;

import android.graphics.Color;
import com.google.android.gms.maps.model.PolygonOptions;
import java.util.List;

public class Zone {
    private List<Position> listPosition;

    public Zone(List<Position> listPosition) {
        this.listPosition = listPosition;
    }

    public PolygonOptions addPolygon() {
        PolygonOptions options = new PolygonOptions()
                .fillColor(Color.argb(33,26, 149,244))
                .strokeWidth(3)
                .strokeColor(Color.BLUE);
        for(int i = 0; i < listPosition.size(); ++i) {
            options.add(listPosition.get(i).asLatLng());
        }
        return options;
    }
}
