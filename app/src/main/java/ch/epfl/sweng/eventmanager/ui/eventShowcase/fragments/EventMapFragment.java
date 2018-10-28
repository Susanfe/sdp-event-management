package ch.epfl.sweng.eventmanager.ui.eventShowcase.fragments;

import android.Manifest;
import android.arch.lifecycle.ViewModelProviders;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import ch.epfl.sweng.eventmanager.R;
import ch.epfl.sweng.eventmanager.repository.data.EventLocation;
import ch.epfl.sweng.eventmanager.repository.data.Spot;
import ch.epfl.sweng.eventmanager.ui.eventShowcase.models.SpotsModel;
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
    protected SpotsModel spotsModel;

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    public EventMapFragment() {
        // Required empty public constructor
        super(R.layout.fragment_event_map);
    }

    @Override
    public void onResume() {
        super.onResume();

        if (spotsModel == null) {
            spotsModel = ViewModelProviders.of(requireActivity()).get(SpotsModel.class);
        }

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
            enableMyLocationIfPermitted();
            mMap.setOnMyLocationButtonClickListener(onMyLocationButtonClickListener);
            mMap.setOnMyLocationClickListener(onMyLocationClickListener);


        });
    }

    private void setUpClusterer() {
        mClusterManager = new ClusterManager<>(getActivity(), mMap);

        // Point the map's listeners at the listeners implemented by the cluster
        // manager.
        mMap.setOnCameraIdleListener(mClusterManager);
        mMap.setOnMarkerClickListener(mClusterManager);

        mClusterManager.setAnimation(true);

        this.spotsModel.getSpots().observe(getActivity(), spots -> {
            if (spots == null)
                return;

            // 1. clear old spots
            mClusterManager.clearItems();

            // 2. Add new spots
            for (Spot s : spots) {
                System.out.println(s);
                mClusterManager.addItem(s);
            }
        });
    }

    private void enableMyLocationIfPermitted() {
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        } else if (mMap != null) {
            mMap.setMyLocationEnabled(true);
        }
    }

    private GoogleMap.OnMyLocationButtonClickListener onMyLocationButtonClickListener = new GoogleMap.OnMyLocationButtonClickListener() {
        @Override
        public boolean onMyLocationButtonClick() {
            Log.i("test:", "buttonCkick");
            Toast.makeText(getActivity(), "MyLocation button clicked", Toast.LENGTH_SHORT).show();
            // Return false so that we don't consume the event and the default behavior still occurs
            // (the camera animates to the user's current position).
            return false;
        }
    };

    private GoogleMap.OnMyLocationClickListener onMyLocationClickListener =  new GoogleMap.OnMyLocationClickListener() {
        @Override
        public void onMyLocationClick(@NonNull Location location) {
            Log.i("test:", "buttonCkickListener");
            Toast.makeText(getActivity(), "Current location:\n" + location, Toast.LENGTH_LONG).show();
        }
    };

}
