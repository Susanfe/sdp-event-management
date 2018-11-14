package ch.epfl.sweng.eventmanager.repository.data;

import android.graphics.Bitmap;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

import java.util.LinkedList;
import java.util.List;

import ch.epfl.sweng.eventmanager.R;

/**
 * Represents a place into the event
 *
 * @author Robin Zbinden (274236)
 * @author Stanislas Jouven (260580)
 */
public class Spot implements ClusterItem {

    /**
     * The name for the spot
     */
    private String title;
    /**
     * The description of the spot
     */
    private SpotType spotType;
    /**
     * The position of the spot
     */
    private Position position;
    /**
     * The ressource picture
     */
    private Bitmap bitmap;
    /**
     * List of scheduledItem
     */
    private List<ScheduledItem> scheduleList;


    Spot(String title, SpotType spotType, double latitude, double longitude, Bitmap bitmap) {
        this.title = title;
        this.spotType = spotType;
        this.position = new Position(latitude, longitude);
        Log.i("TAGBITMAP", String.valueOf(bitmap==null));
        this.bitmap = bitmap;
    }

    public Spot() {}

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String getSnippet() {
        if(scheduleList != null && scheduleList.size() != 0){
            return spotType == null ? null : spotType.getName() + "- Click to see the schedule";
        }
        return spotType == null ? null : spotType.getName();
    }

    @Override
    public LatLng getPosition() {
        return position.asLatLng();
    }

    public SpotType getSpotType() {
        return spotType;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setScheduleList(List<ScheduledItem> scheduleList) {
        this.scheduleList = new LinkedList<>();
        for (ScheduledItem s: scheduleList) {
            if(s.getItemLocation().equals(getTitle())) {
                this.scheduleList.add(s);
            }
        }
    }

    public List<ScheduledItem> getScheduleList() {
        return scheduleList;
    }

    public void setImage(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
}
