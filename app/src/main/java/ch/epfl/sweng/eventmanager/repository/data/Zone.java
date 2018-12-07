package ch.epfl.sweng.eventmanager.repository.data;

import android.content.Context;
import android.graphics.Color;
import com.google.android.gms.maps.model.PolygonOptions;
import java.util.List;

import ch.epfl.sweng.eventmanager.R;

public class Zone {
    private List<Position> positions;

    public Zone(List<Position> positions) {
        this.positions = positions;
    }

    public Zone() {}

    public PolygonOptions addPolygon(Context context) {
        PolygonOptions options = new PolygonOptions()
                .fillColor(context.getResources().getColor(R.color.overlay_blue))
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
