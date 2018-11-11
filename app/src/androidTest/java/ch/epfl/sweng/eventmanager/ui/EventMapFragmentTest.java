package ch.epfl.sweng.eventmanager.ui;

import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.test.espresso.contrib.DrawerActions;
import android.support.test.espresso.contrib.NavigationViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiSelector;

import com.google.android.gms.dynamic.IObjectWrapper;
import com.google.android.gms.internal.maps.zzt;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.epfl.sweng.eventmanager.R;
import ch.epfl.sweng.eventmanager.ui.eventSelector.EventPickingActivity;
import ch.epfl.sweng.eventmanager.ui.eventShowcase.EventShowcaseActivity;

import static android.os.SystemClock.sleep;
import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.fail;

@RunWith(AndroidJUnit4.class)
public class EventMapFragmentTest {

    private final static int DIFFERENCE_TO_SNIPPET = 80;

    @Rule
    public final ActivityTestRule<EventShowcaseActivity> mActivityRule =
            new ActivityTestRule<>(EventShowcaseActivity.class);

    @Test
    public void eventMapTest() {
        //TODO: separate into several methods
        Intent intent = new Intent();
        // Opens Sysmic Event
        intent.putExtra(EventPickingActivity.SELECTED_EVENT_ID, 2);
        mActivityRule.launchActivity(intent);

        onView(withId(R.id.drawer_layout))
                .perform(DrawerActions.open());

        // Display Map Events
        onView(withId(R.id.nav_view))
                .perform(NavigationViewActions.navigateTo(R.id.nav_map));
        onView(withId(R.id.text_test)).check(matches(withText("everything is ready")));
        sleep(500);

        //all this part doesn't work with travis
        UiDevice device = UiDevice.getInstance(getInstrumentation());

        onView(withId(R.id.drawer_layout))
                .perform(DrawerActions.open());

        onView(withId(R.id.nav_view))
                .perform(NavigationViewActions.navigateTo(R.id.nav_main));

        onView(withId(R.id.drawer_layout))
                .perform(DrawerActions.open());

        onView(withId(R.id.nav_view))
                .perform(NavigationViewActions.navigateTo(R.id.nav_map));

        sleep(800);





        // Clicks on the EPFL marker
        /*try {
            UiObject marker = device.findObject(new UiSelector().descriptionContains("EPFL"));
            marker.click();
            int x = marker.getBounds().centerX();
            int y = marker.getBounds().centerY();
            device.click(x, y-DIFFERENCE_TO_SNIPPET);
        } catch (UiObjectNotFoundException e) {
            e.printStackTrace();
            fail("Fail on finding the marker");
        }*/

        //TODO: Test the different components of the map
        /*
        // Clicks on the zoom in
        try {
            UiObject compass = device.findObject(new UiSelector().descriptionContains("ZOOM IN"));
            compass.click();
        } catch (UiObjectNotFoundException e) {
            e.printStackTrace();
            fail("Fail on finding the zoom in");
        }*/
    }
    /*
    @Test //
    public void popUpWork(){

        mActivityRule.getActivity().onMarkerClick(new Marker(new zzt() {
            @Override
            public void remove() throws RemoteException {
                //for overriding
            }

            @Override
            public String getId() throws RemoteException {
                return null;
            }

            @Override
            public void setPosition(LatLng latLng) throws RemoteException {
                //for overriding
            }

            @Override
            public LatLng getPosition() throws RemoteException {
                return new LatLng(1, 2);
            }

            @Override
            public void setTitle(String s) throws RemoteException {
                //for overriding
            }

            @Override
            public String getTitle() throws RemoteException {
                return null;
            }

            @Override
            public void setSnippet(String s) throws RemoteException {
                //for overriding
            }

            @Override
            public String getSnippet() throws RemoteException {
                return null;
            }

            @Override
            public void setDraggable(boolean b) throws RemoteException {
                //for overriding
            }

            @Override
            public boolean isDraggable() throws RemoteException {
                return false;
            }

            @Override
            public void showInfoWindow() throws RemoteException {
                //for overriding
            }

            @Override
            public void hideInfoWindow() throws RemoteException {
                //for overriding
            }

            @Override
            public boolean isInfoWindowShown() throws RemoteException {
                return false;
            }

            @Override
            public void setVisible(boolean b) throws RemoteException {
                //for overriding
            }

            @Override
            public boolean isVisible() throws RemoteException {
                return false;
            }

            @Override
            public boolean zzj(zzt zzt) throws RemoteException {
                return false;
            }

            @Override
            public int zzi() throws RemoteException {
                return 0;
            }

            @Override
            public void zzg(IObjectWrapper iObjectWrapper) throws RemoteException {
                //for overriding
            }

            @Override
            public void setAnchor(float v, float v1) throws RemoteException {
                //for overriding
            }

            @Override
            public void setFlat(boolean b) throws RemoteException {
                //for overriding
            }

            @Override
            public boolean isFlat() throws RemoteException {
                return false;
            }

            @Override
            public void setRotation(float v) throws RemoteException {
                //for overriding
            }

            @Override
            public float getRotation() throws RemoteException {
                return 0;
            }

            @Override
            public void setInfoWindowAnchor(float v, float v1) throws RemoteException {
                //for overriding
            }

            @Override
            public void setAlpha(float v) throws RemoteException {
                //for overriding
            }

            @Override
            public float getAlpha() throws RemoteException {
                return 0;
            }

            @Override
            public void setZIndex(float v) throws RemoteException {
                //for overriding
            }

            @Override
            public float getZIndex() throws RemoteException {
                return 0;
            }

            @Override
            public void zze(IObjectWrapper iObjectWrapper) throws RemoteException {
                //for overriding
            }

            @Override
            public IObjectWrapper zzj() throws RemoteException {
                return null;
            }

            @Override
            public IBinder asBinder() {
                return null;
            }
        }));

    }*/

}
