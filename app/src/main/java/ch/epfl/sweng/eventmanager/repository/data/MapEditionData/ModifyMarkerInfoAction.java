package ch.epfl.sweng.eventmanager.repository.data.MapEditionData;

import com.google.android.gms.maps.model.Marker;

import java.util.List;

public class ModifyMarkerInfoAction extends MapEditionAction {

    private String ancientTitle;
    private String title;

    private String ancientSnippet;
    private String snippet;

    public ModifyMarkerInfoAction(EventEditionTag tag, String ancientTitle, String currentTitle,
                                  String ancientSnippet, String currentSnippet) {

        if (tag == null || ancientTitle == null || currentTitle == null ||
                ancientSnippet == null || currentSnippet == null) {
            throw new IllegalArgumentException();
        }

        this.tag = tag;
        this.title = currentTitle;
        this.ancientTitle=ancientTitle;
        this.snippet=currentSnippet;
        this.ancientSnippet=ancientSnippet;

    }

    @Override
    public boolean revert(List<Marker> markers) {
        Marker marker = findMarkerByTag(markers);
        if (marker != null && marker.getTitle().equals(title) && marker.getSnippet().equals(snippet)) {
            marker.setTitle(ancientTitle);
            marker.setSnippet(ancientSnippet);
            return true;
        }

        return false;
    }
}
