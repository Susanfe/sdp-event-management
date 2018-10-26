package ch.epfl.sweng.eventmanager.ui.eventShowcase.fragments;

import android.arch.lifecycle.ViewModelProviders;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.TextView;
import ch.epfl.sweng.eventmanager.R;
import ch.epfl.sweng.eventmanager.data.EventLocation;
import ch.epfl.sweng.eventmanager.data.Spot;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterManager;

/**
 * Display the map and his features
 *
 * @author Robin Zbinden (274236)
 * @author Stanislas Jouven (260580)
 */
public class EventMapFragment extends AbstractShowcaseFragment {

    private static final String TAG = "EventMapFragment";
    private static final float ZOOMLEVEL = 19.0f; //This goes up to 21
    private GoogleMap mMap;
    private ClusterManager<Spot> mClusterManager;

    public EventMapFragment() {
        // Required empty public constructor
        super(R.layout.fragment_event_map);
    }

    @Override
    public void onResume() {
        super.onResume();

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.mapFragment);
        if (mapFragment == null) {
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            mapFragment = SupportMapFragment.newInstance();
            fragmentTransaction.replace(R.id.mapFragment, mapFragment).commit();
        }
        if (mapFragment != null) {
            mapFragment.getMapAsync(googleMap -> {
                mMap = googleMap;
                if (mMap != null) {
                    setUpMap();
                    setUpClusterer();
                }
            });
        }

        TextView textView = (TextView) view.findViewById(R.id.text_test);
        textView.setText("everything is ready");

    }

    private void setUpMap() {
        model.getEvent().observe(getActivity(), event -> {
            if (event == null || event.getLocation() == null)
                return;

            EventLocation loc = event.getLocation();
            LatLng place = loc.getPosition().asLatLng();
            mMap.addMarker(new MarkerOptions().position(place).title(loc.getName()));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(place, ZOOMLEVEL));

            mMap.getUiSettings().setCompassEnabled(true);
            mMap.getUiSettings().setZoomControlsEnabled(true);
            mMap.getUiSettings().setMapToolbarEnabled(true);
        });


    }

    private void setUpClusterer() {
        mClusterManager = new ClusterManager<>(getActivity(), mMap);

        // Point the map's listeners at the listeners implemented by the cluster
        // manager.
        mMap.setOnCameraIdleListener(mClusterManager);
        mMap.setOnMarkerClickListener(mClusterManager);

        mClusterManager.setAnimation(true);

        model.getEvent().observe(getActivity(), event -> {
            if (event == null || event.getSpotList() == null)
                return;

            // 1. clear old spots
            mClusterManager.clearItems();

            // 2. Add new spots
            for (Spot s : event.getSpotList()) {
                System.out.println(s);
                mClusterManager.addItem(s);
            }
        });
    }


}
