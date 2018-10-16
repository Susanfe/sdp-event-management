package ch.epfl.sweng.eventmanager.ui.eventShowcase;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import android.util.Log;
import android.widget.TextView;

import ch.epfl.sweng.eventmanager.repository.data.Spot;
import ch.epfl.sweng.eventmanager.repository.data.SpotType;
import ch.epfl.sweng.eventmanager.ui.eventSelector.EventPickingActivity;
import ch.epfl.sweng.eventmanager.viewmodel.ViewModelFactory;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterManager;

import java.util.ArrayList;
import java.util.List;

import ch.epfl.sweng.eventmanager.R;
import dagger.android.AndroidInjection;

import javax.inject.Inject;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    private static String TAG = "MapsActivity";

    @Inject
    ViewModelFactory factory;

    private EventShowcaseModel model;
    private GoogleMap mMap;

    private ClusterManager<Spot> mClusterManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        Intent intent = getIntent();
        int eventID = intent.getIntExtra(EventPickingActivity.SELECTED_EVENT_ID, -1);
        // TODO: fetch event, update displayed values.
        if (eventID <= 0) { // Suppose that negative or null event ID are invalids
            // TODO: find a way to pass the event ID between the different views
            Log.e(TAG, "Got invalid event ID#" + eventID + ".");
        } else {
            this.model = ViewModelProviders.of(this, factory).get(EventShowcaseModel.class);
            this.model.init(eventID);

            mapFragment.getMapAsync(this);
        }
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        if (this.model == null) {
            return;
        }

        mMap = googleMap;

        float zoomLevel = 19.0f; //This goes up to 21

        this.model.getLocation().observe(this, loc -> {
            if (loc != null) {
                LatLng place = new LatLng(loc.getLatitude(), loc.getLongitude());
                mMap.addMarker(new MarkerOptions().position(place).title(loc.getName()));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(place, zoomLevel));

                setUpClusterer(setFakeSpot());
                mMap.getUiSettings().setCompassEnabled(true);
                mMap.getUiSettings().setZoomControlsEnabled(true);
                mMap.getUiSettings().setMapToolbarEnabled(true);
            }
        });

    }

    private List<Spot> setFakeSpot() {
        Spot spot1 = new Spot("Grande scène", SpotType.SCENE, 46.517799, 6.566737);
        Spot spot2 = new Spot("Satellite", SpotType.BAR, 46.520433, 6.567822);
        Spot spot3 = new Spot("test3", SpotType.NURSERY, 46.523, 6.567822);
        Spot spot4 = new Spot("test4", SpotType.WC, 46.520433, 6.5679);
        Spot spot5 = new Spot("test5", SpotType.INFORMATION, 46.520433, 6.55555);
        List<Spot> spotList= new ArrayList<>();
        spotList.add(spot1);
        spotList.add(spot2);
        spotList.add(spot3);
        spotList.add(spot4);
        spotList.add(spot5);
        return spotList;
    }


    private void setUpClusterer(List<Spot> spotList) {

        mClusterManager = new ClusterManager<Spot>(this, mMap);

        // Point the map's listeners at the listeners implemented by the cluster
        // manager.
        mMap.setOnCameraIdleListener(mClusterManager);
        mMap.setOnMarkerClickListener(mClusterManager);

        for(Spot s: spotList){
            mClusterManager.addItem(s);
        }
        mClusterManager.setAnimation(true);
    }
}
