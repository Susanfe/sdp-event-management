package ch.epfl.sweng.eventmanager.repository.data.MapEditionData.history;

import com.google.android.gms.maps.model.Marker;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import ch.epfl.sweng.eventmanager.repository.data.MapEditionData.EventEditionTag;
import ch.epfl.sweng.eventmanager.repository.data.MapEditionData.MarkerType;
import ch.epfl.sweng.eventmanager.repository.data.SpotType;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class ModifyMarkerInfoActionTest {
    private EventEditionTag tag;
    private List<Marker> markers = new ArrayList<>();

    private String ancientTitle = "Ancient Title";
    private String newTitle = "New Title";
    private String ancientSnippet = "Ancient snippet";
    private String newSnippet = "New Snippet";

    @Before
    public void setUp() {
        tag = EventEditionTag.createSpotTag(0, SpotType.BAR);
    }

    @Test
    public void createModifyInfoAction() {
        // No comparison between two actions should be made as two actions cannot be the same,
        // Two actions can have same fields values and be separate modifications
        ModifyMarkerInfoAction action = new ModifyMarkerInfoAction(tag, ancientTitle, newTitle,
                ancientSnippet, newSnippet, false);

        assertThat(action.getMarkerType(), is(MarkerType.SPOT));
    }

    @Test(expected = IllegalArgumentException.class)
    public void createNullTagAction(){
        new ModifyMarkerInfoAction(null, ancientTitle, newTitle,
                ancientSnippet, newSnippet, false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void createNullAncientTitleAction(){
        new ModifyMarkerInfoAction(tag, null, newTitle,
                ancientSnippet, newSnippet, false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void createNullCurrentTitleAction(){
        new ModifyMarkerInfoAction(tag, ancientTitle, null,
                ancientSnippet, newSnippet, false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void createNullAncientSnippetAction(){
        new ModifyMarkerInfoAction(tag, ancientTitle, newTitle,
                null, newSnippet, false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void createNullCurrentSnippetAction(){
        new ModifyMarkerInfoAction(null, ancientTitle, newTitle,
                ancientSnippet, null, false);
    }

    @Test
    public void verifyWasIssuedByCreation() {
        ModifyMarkerInfoAction action = new ModifyMarkerInfoAction(tag, ancientTitle, newTitle,
                ancientSnippet, newSnippet, false);

        ModifyMarkerInfoAction action2 = new ModifyMarkerInfoAction(tag, ancientTitle, newTitle,
                ancientSnippet, newSnippet, true);

        assertThat(action.wasIssudByCreation(), is(false));
        assertThat(action2.wasIssudByCreation(), is(true));
    }

    @Test
    public void revertOnEmptyList() {
        // Testing with markers is impossible because Marker cannot be directly instanciated
        // Plus mocking is not allowed by mockito because the class is final
        ModifyMarkerInfoAction action = new ModifyMarkerInfoAction(tag, ancientTitle, newTitle,
                ancientSnippet, newSnippet, false);

        boolean result = action.revert(markers, null);
        assertThat(result, is(false));
        assertThat(markers.size(), is(0));
    }

}