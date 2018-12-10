package ch.epfl.sweng.eventmanager.repository.data;

import android.graphics.Color;
import com.google.android.gms.maps.model.PolygonOptions;
import java.util.List;

public class Zone {
    private List<Position> positions;

    private static final int OVERLAY_INSIDE_COLOR = Color.argb(33, 24, 149, 244);

    public Zone(List<Position> positions) {
        this.positions = positions;
    }

    public Zone() {}

    public PolygonOptions addPolygon() {
        PolygonOptions options = new PolygonOptions()
                .fillColor(OVERLAY_INSIDE_COLOR)
                .strokeWidth(3)
                .strokeColor(Color.BLUE);
        for(int i = 0; i < positions.size(); ++i) {
            options.add(positions.get(i).asLatLng());
        }
        return options;
    }

    public List<Position> getPositions() {
        return positions;
    }
}
