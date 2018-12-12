package ch.epfl.sweng.eventmanager.repository.data.MapEditionData.history;

import android.util.SparseArray;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import java.util.List;

import ch.epfl.sweng.eventmanager.repository.data.MapEditionData.EventEditionTag;

public class ModifyMarkerInfoAction extends MapEditionAction {

    // states whether the modification is issued by a CreationAction
    private boolean issuedByCreation;

    private String ancientTitle;
    private String title;

    private String ancientSnippet;
    private String snippet;

    public ModifyMarkerInfoAction(EventEditionTag tag, String ancientTitle, String currentTitle,
                                  String ancientSnippet, String currentSnippet, boolean issuedByCreation) {

        if (tag == null || ancientTitle == null || currentTitle == null ||
                ancientSnippet == null || currentSnippet == null) {
            throw new IllegalArgumentException();
        }

        this.tag = tag;
        this.title = currentTitle;
        this.ancientTitle=ancientTitle;
        this.snippet=currentSnippet;
        this.ancientSnippet=ancientSnippet;
        this.issuedByCreation = issuedByCreation;
    }

    @Override
    public boolean revert(List<Marker> markers, SparseArray<LatLng> positions) {
        Marker marker = findMarkerByTag(markers);

        if (marker != null && marker.getTitle().equals(title) && marker.getSnippet().equals(snippet)) {
            marker.setTitle(ancientTitle);
            marker.setSnippet(ancientSnippet);
            return true;
        }

        return false;
    }

    /**
     * States whether the action was issued by a creation action.
     * @return true if was issued by a creation action, false otherwise.
     */
    public boolean wasIssudByCreation() {
        return issuedByCreation;
    }
}
