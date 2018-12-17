package ch.epfl.sweng.eventmanager.ui.event.interaction.fragments.map;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterItem;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import com.google.maps.android.ui.IconGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProviders;
import butterknife.BindView;
import butterknife.ButterKnife;
import ch.epfl.sweng.eventmanager.R;
import ch.epfl.sweng.eventmanager.repository.data.EventLocation;
import ch.epfl.sweng.eventmanager.repository.data.Position;
import ch.epfl.sweng.eventmanager.ui.event.interaction.fragments.AbstractShowcaseFragment;
import ch.epfl.sweng.eventmanager.repository.data.Spot;
import ch.epfl.sweng.eventmanager.repository.data.Zone;
import ch.epfl.sweng.eventmanager.ui.event.interaction.EventShowcaseActivity;
import ch.epfl.sweng.eventmanager.ui.event.interaction.fragments.map.MultiDrawable;
import ch.epfl.sweng.eventmanager.ui.event.interaction.fragments.schedule.ScheduleParentFragment;
import ch.epfl.sweng.eventmanager.ui.event.interaction.models.ScheduleViewModel;
import ch.epfl.sweng.eventmanager.ui.event.interaction.models.SpotsModel;
import ch.epfl.sweng.eventmanager.ui.event.interaction.models.ZoneModel;
import ch.epfl.sweng.eventmanager.users.Role;
import ch.epfl.sweng.eventmanager.users.Session;
import ch.epfl.sweng.eventmanager.viewmodel.ViewModelFactory;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.*;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterItem;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import com.google.maps.android.ui.IconGenerator;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

/**
 * Display the map and his features
 *
 * @author Robin Zbinden (274236)
 * @author Stanislas Jouven (260580)
 */
public class EventMapFragment extends AbstractShowcaseFragment implements
        ClusterManager.OnClusterClickListener<Spot>,
        ClusterManager.OnClusterItemInfoWindowClickListener<Spot>, OnMapReadyCallback {

    private static final float ZOOMLEVEL = 15.0f; //This goes up to 21

    Polygon eventOverlay;

    @Inject
    ViewModelFactory factory;

    private static final String TAG = "EventMapFragment";

    private ClusterManager<Spot> mClusterManager;
    SpotsModel spotsModel;
    ZoneModel zonesModel;
    private ScheduleViewModel scheduleViewModel;
    private Spot clickedClusterItem;
    private GoogleMap googleMap;

    @Inject
    public Session session;

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    @BindView(R.id.mapview)
    MapView mapView;

    public EventMapFragment() {
        super(R.layout.fragment_event_map);
    }

    public static EventMapFragment newInstance() {
        return new EventMapFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Indicates to the fragment that EventShowcaseActivity has a menu
        setHasOptionsMenu(true);

        if (mapView != null) {
            mapView.onCreate(savedInstanceState);
        }
        if (spotsModel == null) {
            spotsModel = ViewModelProviders.of(requireActivity(), factory).get(SpotsModel.class);
        }
        if (zonesModel == null) {
            zonesModel = ViewModelProviders.of(requireActivity(), factory).get(ZoneModel.class);
        }
        if (scheduleViewModel == null) {
            scheduleViewModel = ViewModelProviders.of(requireActivity(), factory).get(ScheduleViewModel.class);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (session.isLoggedIn() && session.isClearedFor(Role.ADMIN,
                ((EventShowcaseActivity) Objects.requireNonNull(getActivity())).getEvent()))
            menu.findItem(R.id.menu_showcase_activity_map_edition_edit).setVisible(true);

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_showcase_activity_map_edition_edit:
                ((EventShowcaseActivity) Objects.requireNonNull(getActivity())).callChangeFragment(
                        EventShowcaseActivity.FragmentType.MAP_EDITION, true);
                return true;

            default:
                // Action was not consumed (calls activity method)
                return false;
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_event_map, container, false);
        ButterKnife.bind(this, v);
        mapView.onCreate(savedInstanceState);
        //get the map asynchronously
        mapView.getMapAsync(this);
        return v;
    }

    /**
     * Setup the map : location, camera and controls
     */
    private void setUpMap() {
        this.model.getEvent().observe(this, event -> {
            if (event == null) {
                throw new NullPointerException("event is null");
            }

            EventLocation loc;
            if (event.getLocation() != null) {
                loc = event.getLocation();
            } else {
                loc = new EventLocation("EPFL", Position.EPFL);
                Toast.makeText(getContext(), getString(R.string.map_location_of_event_null), Toast.LENGTH_LONG).show();
            }

            LatLng place = loc.getPosition().asLatLng();
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(place, ZOOMLEVEL));
            googleMap.getUiSettings().setCompassEnabled(true);
            googleMap.getUiSettings().setZoomControlsEnabled(true);
            googleMap.getUiSettings().setMapToolbarEnabled(true);
            enableMyLocationIfPermitted();
            googleMap.setOnMyLocationButtonClickListener(onMyLocationButtonClickListener);
            googleMap.setOnMyLocationClickListener(onMyLocationClickListener);
        });
    }

    /**
     * Setup the overlay which represent the event
     */
    private void setUpOverlay() {
        this.zonesModel.getZone().observe(this, zones -> {
            if (zones != null) {
                for (Zone z : zones) {
                    eventOverlay = googleMap.addPolygon(z.addPolygon());
                }
            }
        });
    }

    /**
     * Setup the cluster manager
     */
    private void setUpCluster() {
        mClusterManager = new ClusterManager<>(requireActivity(), googleMap);
        mClusterManager.setOnClusterItemInfoWindowClickListener(this);
        mClusterManager.setOnClusterClickListener(this);
        mClusterManager.setRenderer(new SpotRenderer(requireActivity()));
        mClusterManager.setOnClusterItemClickListener(spot -> {
            clickedClusterItem = spot;
            return false;
        });
        mClusterManager.getMarkerCollection().setOnInfoWindowAdapter(new MyCustomAdapterForItems());
        mClusterManager.setAnimation(true);

        googleMap.setOnMarkerClickListener(mClusterManager);
        googleMap.setOnInfoWindowClickListener(mClusterManager);
        googleMap.setInfoWindowAdapter(mClusterManager.getMarkerManager());
        googleMap.setOnCameraIdleListener(mClusterManager);
        addItemToCluster();
    }

    /**
     * Add the spots to the cluster on the map
     */
    private void addItemToCluster() {
        this.scheduleViewModel.getScheduledItems().observe(this,
                items -> this.spotsModel.getSpots().observe(this, spots -> {
            if (spots == null) {
                return;
            }
            // 1. clear old spots
            mClusterManager.clearItems();
            // 2. Add new spots
            for (Spot s : spots) {
                s.setScheduleList(items);
            }
            mClusterManager.addItems(spots);
            mClusterManager.cluster();
        }));

    }

    /**
     * Enable location if location permissions are allowed by the user for the app
     */
    private void enableMyLocationIfPermitted() {
        if (ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        } else if (googleMap != null) {
            googleMap.setMyLocationEnabled(true);
        }
    }

    private GoogleMap.OnMyLocationButtonClickListener onMyLocationButtonClickListener = () -> {
        // (the camera animates to the user's current position).
        return false;
    };

    @Override
    public boolean onClusterClick(Cluster<Spot> cluster) {

        // Zoom in the cluster. Need to create LatLngBounds and including all the cluster items
        // inside of bounds, then animate to center of the bounds.

        // Create the builder to collect all essential cluster items for the bounds.
        LatLngBounds.Builder builder = LatLngBounds.builder();
        for (ClusterItem item : cluster.getItems()) {
            builder.include(item.getPosition());
        }
        // Get the LatLngBounds
        final LatLngBounds bounds = builder.build();

        // Animate camera to the bounds
        googleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100));

        return true;
    }

    @Override
    public void onClusterItemInfoWindowClick(Spot spot) {
        if (spot != null && spot.getScheduleList() != null && spot.getScheduleList().size() != 0) {
            goToSchedule();
        }
    }

    private void goToSchedule() {
        ScheduleParentFragment scheduleParentFragment =
                ScheduleParentFragment.newInstance(clickedClusterItem.getTitle());
        ((EventShowcaseActivity) requireActivity()).changeFragment(scheduleParentFragment, true);
    }

    /**
     * Callback when the map is ready : we setup location, clusters and overlay
     *
     * @param googleMap
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        setUpMap();
        setUpCluster();
        setUpOverlay();
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
    }

    /**
     * how the markers and clusters are rendered
     */
    private class SpotRenderer extends DefaultClusterRenderer<Spot> {
        private final IconGenerator mIconGenerator = new IconGenerator(requireActivity().getApplicationContext());
        private final IconGenerator mClusterIconGenerator =
                new IconGenerator(requireActivity().getApplicationContext());
        private final ImageView mImageView;
        private final ImageView mClusterImageView;
        private final int mDimension;

        public SpotRenderer(Context context) {
            super(context, googleMap, mClusterManager);

            if (context == null) {
                throw new NullPointerException("context is null");
            }

            View multiProfile = getLayoutInflater().inflate(R.layout.custom_marker,
                    new LinearLayout(getContext()));
            mClusterIconGenerator.setContentView(multiProfile);
            mClusterImageView = multiProfile.findViewById(R.id.image);

            mImageView = new ImageView(requireActivity());
            mDimension = (int) context.getResources().getDimension(R.dimen.custom_marker_size);
            mImageView.setLayoutParams(new ViewGroup.LayoutParams(mDimension, mDimension));
            int padding = (int) context.getResources().getDimension(R.dimen.custom_marker_padding);
            mImageView.setPadding(padding, padding, padding, padding);
            mIconGenerator.setContentView(mImageView);
        }

        @Override
        protected void onBeforeClusterItemRendered(Spot spot, MarkerOptions markerOptions) {
            // Draw a single spot.
            // Set the info window to show the description.
            mImageView.setImageBitmap(spot.getBitmap());
            Bitmap icon = mIconGenerator.makeIcon();
            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(icon)).title(spot.getTitle());
        }

        @Override
        protected void onBeforeClusterRendered(Cluster<Spot> cluster, MarkerOptions markerOptions) {
            // Draw multiple people.
            // Note: this method runs on the UI thread. Don't spend too much time in here (like in this example).
            List<Drawable> spotImages = new ArrayList<>(Math.min(4, cluster.getSize()));
            int width = mDimension;
            int height = mDimension;

            for (Spot p : cluster.getItems()) {
                // Draw at most 4 images on the same cluster
                if (spotImages.size() == 4) {
                    break;
                }
                Drawable drawable = new BitmapDrawable(requireContext().getResources(), p.getBitmap());
                drawable.setBounds(0, 0, width, height);
                spotImages.add(drawable);
            }
            MultiDrawable multiDrawable = new MultiDrawable(spotImages);
            multiDrawable.setBounds(0, 0, width, height);

            mClusterImageView.setImageDrawable(multiDrawable);
            Bitmap icon = mClusterIconGenerator.makeIcon(String.valueOf(cluster.getSize()));
            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(icon));
        }

        @Override
        protected boolean shouldRenderAsCluster(Cluster cluster) {
            // Always render clusters.
            return cluster.getSize() > 1;
        }
    }

    /**
     * Adapter used to change the format of the snippets of the markers
     */
    private class MyCustomAdapterForItems implements GoogleMap.InfoWindowAdapter {

        private final View myContentsView;

        MyCustomAdapterForItems() {
            myContentsView = getLayoutInflater().inflate(R.layout.custom_info_window,
                    new LinearLayout(getContext()));
        }

        @Override
        public View getInfoWindow(Marker marker) {
            return null;
        }

        @Override
        public View getInfoContents(Marker marker) {
            TextView tvTitle = myContentsView.findViewById(R.id.name);
            TextView tvSnippet = myContentsView.findViewById(R.id.details);
            TextView tvClickToSchedule = myContentsView.findViewById(R.id.scheduleClick);

            if (clickedClusterItem.getScheduleList() != null && clickedClusterItem.getScheduleList().size() != 0) {
                tvClickToSchedule.setVisibility(View.VISIBLE);
                tvClickToSchedule.setText(getResources().getString(R.string.click_to_see_schedule));
            } else {
                tvClickToSchedule.setVisibility(View.GONE);
            }
            tvTitle.setText(clickedClusterItem.getTitle());
            tvSnippet.setText(clickedClusterItem.getSnippet());

            return myContentsView;
        }

    }
}
