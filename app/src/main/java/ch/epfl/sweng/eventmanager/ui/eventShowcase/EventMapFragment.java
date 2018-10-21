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
import com.google.maps.android.clustering.ClusterManager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ch.epfl.sweng.eventmanager.R;
import ch.epfl.sweng.eventmanager.repository.data.Event;
import ch.epfl.sweng.eventmanager.repository.data.Spot;
import ch.epfl.sweng.eventmanager.repository.data.SpotType;
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
    private ClusterManager<Spot> mClusterManager;

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
                        float zoomLevel = 19.0f; //This goes up to 21
                        model.getLocation().observe(getActivity(), loc -> {
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

                }
            });

        }

        return view;
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

        mClusterManager = new ClusterManager<Spot>(getActivity(), mMap);

        // Point the map's listeners at the listeners implemented by the cluster
        // manager.
        mMap.setOnCameraIdleListener(mClusterManager);
        mMap.setOnMarkerClickListener(mClusterManager);

        for(Spot s: spotList){
            mClusterManager.addItem(s);
        }
        mClusterManager.setAnimation(true);
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
