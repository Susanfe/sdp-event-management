package ch.epfl.sweng.eventmanager.ui.event.interaction.fragments;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterItem;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import com.google.maps.android.ui.IconGenerator;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;
import ch.epfl.sweng.eventmanager.R;
import ch.epfl.sweng.eventmanager.repository.data.EventLocation;
import ch.epfl.sweng.eventmanager.repository.data.MultiDrawable;
import ch.epfl.sweng.eventmanager.repository.data.Spot;
import ch.epfl.sweng.eventmanager.repository.data.Zone;
import ch.epfl.sweng.eventmanager.ui.event.interaction.EventShowcaseActivity;
import ch.epfl.sweng.eventmanager.ui.event.interaction.fragments.schedule.ScheduleParentFragment;
import ch.epfl.sweng.eventmanager.ui.event.interaction.models.ScheduleViewModel;
import ch.epfl.sweng.eventmanager.ui.event.interaction.models.SpotsModel;
import ch.epfl.sweng.eventmanager.ui.event.interaction.models.ZoneModel;
import ch.epfl.sweng.eventmanager.viewmodel.ViewModelFactory;
import dagger.android.support.AndroidSupportInjection;

/**
 * Display the map and his features
 *
 * @author Robin Zbinden (274236)
 * @author Stanislas Jouven (260580)
 */
public class EventMapFragment extends AbstractShowcaseFragment implements
        ClusterManager.OnClusterClickListener<Spot>,
        ClusterManager.OnClusterItemInfoWindowClickListener<Spot>  {

    private static final String TAG = "EventMapFragment";
    private static final float ZOOMLEVEL = 15.0f; //This goes up to 21
    public static final String TAB_NB_KEY = "ch.epfl.sweng.eventmanager.TAB_NB_KEY";
    private GoogleMap mMap;

    @Inject
    ViewModelFactory factory;
    private ClusterManager<Spot> mClusterManager;
    protected SpotsModel spotsModel;
    protected ZoneModel zonesModel;
    protected ScheduleViewModel scheduleViewModel;
    private Spot clickedClusterItem;

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    public EventMapFragment() {
        // Required empty public constructor
        super(R.layout.fragment_event_map);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        AndroidSupportInjection.inject(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();

        if (spotsModel == null) {
            spotsModel = ViewModelProviders.of(requireActivity(), factory).get(SpotsModel.class);
        }

        if(zonesModel == null) {
            zonesModel = ViewModelProviders.of(requireActivity(), factory).get(ZoneModel.class);
        }

        if(scheduleViewModel == null) {
            scheduleViewModel = ViewModelProviders.of(requireActivity(), factory).get(ScheduleViewModel.class);
        }

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapFragment);
        if (mapFragment == null) {
            FragmentManager fragmentManager = getFragmentManager();
            // FIXME handle nullpointerexception
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            mapFragment = SupportMapFragment.newInstance();
            fragmentTransaction.replace(R.id.mapFragment, mapFragment).commit();
        }
        mapFragment.getMapAsync(googleMap -> {
            mMap = googleMap;
            if (mMap != null) {
                setUpMap();
                setUpClusterer();
                setUpOverlay();
            }
        });
    }

    private void setUpMap() {
        // FIXME handle nullpointerException
        model.getEvent().observe(getActivity(), event -> {
            if (event == null || event.getLocation() == null)
                return;

            EventLocation loc = event.getLocation();
            LatLng place = loc.getPosition().asLatLng();
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(place, ZOOMLEVEL));
            mMap.getUiSettings().setCompassEnabled(true);
            mMap.getUiSettings().setZoomControlsEnabled(true);
            mMap.getUiSettings().setMapToolbarEnabled(true);
            enableMyLocationIfPermitted();
            mMap.setOnMyLocationButtonClickListener(onMyLocationButtonClickListener);
            mMap.setOnMyLocationClickListener(onMyLocationClickListener);
        });
    }

    private void setUpOverlay() {
        // FIXME handle nullpointerexception
        this.zonesModel.getZone().observe(getActivity(), zones -> {
            if(zones == null) {
                return;
            }
            for(Zone z: zones) {
                mMap.addPolygon(z.addPolygon());
            }
        });
    }

    private void setUpClusterer() {
        // FIXME handle nullpointerexception
        mClusterManager = new ClusterManager<>(getActivity(), mMap);
        mClusterManager.setOnClusterItemInfoWindowClickListener(this);
        mClusterManager.setOnClusterClickListener(this);
        mClusterManager.setRenderer(new SpotRenderer(getActivity()));
        mClusterManager.setOnClusterItemClickListener(spot -> {
            clickedClusterItem = spot;
            return false;
        });
        mClusterManager.getMarkerCollection().setOnInfoWindowAdapter(new MyCustomAdapterForItems());
        mClusterManager.setAnimation(true);

        mMap.setOnMarkerClickListener(mClusterManager);
        mMap.setOnInfoWindowClickListener(mClusterManager);
        mMap.setInfoWindowAdapter(mClusterManager.getMarkerManager());
        mMap.setOnCameraIdleListener(mClusterManager);

        addItemToCluster();
    }

    private void addItemToCluster() {
        this.scheduleViewModel.getScheduledItems().observe(getActivity(), items -> this.spotsModel.getSpots().observe(getActivity(), spots -> {
            if (spots == null) {
                return;
            }
            // 1. clear old spots
            mClusterManager.clearItems();

            // 2. Add new spots
            for (Spot s : spots) {
                s.setScheduleList(items);
                mClusterManager.addItem(s);
            }
        }));
    }

    private void enableMyLocationIfPermitted() {
        // FIXME handle nullpointerexception
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

    private GoogleMap.OnMyLocationButtonClickListener onMyLocationButtonClickListener = () -> {
        //Toast.makeText(getActivity(), "", Toast.LENGTH_SHORT).show();
        // Return false so that we don't consume the event and the default behavior still occurs
        // (the camera animates to the user's current position).
        return false;
    };

    private GoogleMap.OnMyLocationClickListener onMyLocationClickListener = location ->
        Toast.makeText(getActivity(), "" + location, Toast.LENGTH_LONG).show();

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
        try {
            mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return true;
    }

    @Override
    public void onClusterItemInfoWindowClick(Spot spot) {
        if(spot.getScheduleList() != null && spot.getScheduleList().size() != 0) {
            ScheduleParentFragment scheduleParentFragment = new ScheduleParentFragment();
            Bundle args = new Bundle();
            args.putString(TAB_NB_KEY, clickedClusterItem.getTitle());
            scheduleParentFragment.setArguments(args);
            // FIXME handle nullpointerexception
            ((EventShowcaseActivity)getActivity()).changeFragment(
                    scheduleParentFragment, true);
        }
    }

    private class SpotRenderer extends DefaultClusterRenderer<Spot> {
        // FIXME handlenullpointerexception
        private final IconGenerator mIconGenerator = new IconGenerator(getActivity().getApplicationContext());
        private final IconGenerator mClusterIconGenerator = new IconGenerator(getActivity().getApplicationContext());
        private final ImageView mImageView;
        private final ImageView mClusterImageView;
        private final int mDimension;

        public SpotRenderer(Context context) {
            // FIXME handle nullpointerexception
            super(getActivity(), mMap, mClusterManager);

            View multiProfile = getLayoutInflater().inflate(R.layout.custom_marker, null);

            mClusterIconGenerator.setContentView(multiProfile);
            mClusterImageView = multiProfile.findViewById(R.id.image);

            mImageView = new ImageView(getActivity());
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
                if(spotImages.size() == 4) {
                    break;

                }
                Drawable drawable = new BitmapDrawable(p.getBitmap());
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

    private class MyCustomAdapterForItems implements GoogleMap.InfoWindowAdapter {

        private final View myContentsView;

        MyCustomAdapterForItems() {
            myContentsView = getLayoutInflater().inflate(R.layout.custom_info_window, null);
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

            if(clickedClusterItem.getScheduleList() != null && clickedClusterItem.getScheduleList().size() != 0) {
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
