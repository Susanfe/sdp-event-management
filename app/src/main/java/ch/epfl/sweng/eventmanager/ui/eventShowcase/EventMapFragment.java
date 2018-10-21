package ch.epfl.sweng.eventmanager.ui.eventShowcase;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.Serializable;

import ch.epfl.sweng.eventmanager.R;
import ch.epfl.sweng.eventmanager.repository.data.Event;
import ch.epfl.sweng.eventmanager.ui.eventSelector.EventPickingActivity;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 */
public class EventMapFragment extends Fragment {
    private static final String TAG = "EventMapFragment";
    private EventShowcaseModel model;
    private GoogleMap mMap;
    MapView mMapView;

    public EventMapFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        model = ViewModelProviders.of(getActivity()).get(EventShowcaseModel.class);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_event_map, container, false);



        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.mapFragment);
        if (mapFragment == null) {
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            mapFragment = SupportMapFragment.newInstance();
            fragmentTransaction.replace(R.id.mapFragment, mapFragment).commit();
        }
        if (mapFragment != null) {
            mapFragment.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap googleMap) {
                    mMap = googleMap;
                    if (mMap != null) {

                        model.getLocation().observe(getActivity(), loc -> {
                            if (loc != null) {
                                LatLng place = new LatLng(loc.getLatitude(), loc.getLongitude());
                                mMap.addMarker(new MarkerOptions().position(place).title(loc.getName()));
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(place, 19.0f));

                                //setUpClusterer(setFakeSpot());
                                mMap.getUiSettings().setCompassEnabled(true);
                                mMap.getUiSettings().setZoomControlsEnabled(true);
                                mMap.getUiSettings().setMapToolbarEnabled(true);
                            }
                        });
/*
                        mMap.getUiSettings().setAllGesturesEnabled(true);

                        LatLng place = new LatLng(46.517799, 6.566737);
                        mMap.addMarker(new MarkerOptions().position(place).title("EPFL"));
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(place, 15.0f));
*/
                    }

                }
            });

        }


/*
        mMapView = (MapView) view.findViewById(R.id.map);
        mMapView.
                onCreate(savedInstanceState);

        mMapView.onResume(); // needed to get the map to display immediately

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mMap = googleMap;

                LatLng place = new LatLng(46.517799, 6.566737);
                mMap.addMarker(new MarkerOptions().position(place).title("EPFL"));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(place, 15.0f));
            }
        });
*/


        // FIXME: move MapsActivity's content to EventMapFragment
        //Button openMapButton = (Button) view.findViewById(R.id.open_map_button);
        //openMapButton.setText("Open map");

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
