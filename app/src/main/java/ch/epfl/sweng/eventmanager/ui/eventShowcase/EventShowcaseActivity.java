package ch.epfl.sweng.eventmanager.ui.eventShowcase;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import javax.inject.Inject;

import ch.epfl.sweng.eventmanager.R;
import ch.epfl.sweng.eventmanager.ui.eventSelector.EventPickingActivity;
import ch.epfl.sweng.eventmanager.ui.eventShowcase.fragments.EventMainFragment;
import ch.epfl.sweng.eventmanager.ui.eventShowcase.fragments.EventMapFragment;
import ch.epfl.sweng.eventmanager.ui.eventShowcase.fragments.schedule.ScheduleParentFragment;
import ch.epfl.sweng.eventmanager.ui.eventShowcase.models.EventShowcaseModel;
import ch.epfl.sweng.eventmanager.ui.eventShowcase.models.ScheduleViewModel;
import ch.epfl.sweng.eventmanager.ui.eventShowcase.models.SpotsModel;
import ch.epfl.sweng.eventmanager.userManagement.Role;
import ch.epfl.sweng.eventmanager.userManagement.Session;
import ch.epfl.sweng.eventmanager.viewmodel.ViewModelFactory;
import dagger.android.AndroidInjection;

public class EventShowcaseActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{
    private static final String TAG = "EventShowcaseActivity";

    @Inject
    ViewModelFactory factory;

    private EventShowcaseModel model;
    private ScheduleViewModel scheduleModel;
    private SpotsModel spotsModel;
    private DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_showcase);
        mDrawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = mDrawerLayout.findViewById(R.id.nav_view);

        // Set toolbar as action bar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Add drawer button to the action bar
        if(getSupportActionBar() != null) {
            ActionBar actionbar = getSupportActionBar();
            actionbar.setDisplayHomeAsUpEnabled(true);
            actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);
        }

        // Fetch event from passed ID
        Intent intent = getIntent();
        int eventID = intent.getIntExtra(EventPickingActivity.SELECTED_EVENT_ID, -1);
        if (eventID <= 0) { // Suppose that negative or null event ID are invalids
            Log.e(TAG, "Got invalid event ID#" + eventID + ".");
        } else {
            this.model = ViewModelProviders.of(this, factory).get(EventShowcaseModel.class);
            this.model.init(eventID);

            this.scheduleModel = ViewModelProviders.of(this, factory).get(ScheduleViewModel.class);
            this.scheduleModel.init(eventID);

            this.spotsModel = ViewModelProviders.of(this, factory).get(SpotsModel.class);
            this.spotsModel.init(eventID);

            // Only display admin button if the user is at least staff
            model.getEvent().observe(this, ev -> {
                        if (ev == null) {
                            Log.e(TAG, "Got null model from parent activity");
                            return;
                        }

                        if (Session.isLoggedIn() && Session.isClearedFor(Role.ADMIN, ev)) {
                            MenuItem adminMenuItem = navigationView.getMenu().findItem(R.id.nav_admin);
                            adminMenuItem.setVisible(true);
                        }
                    });

            changeFragment(new EventMainFragment(), true);
        }

        // Handle drawer events
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        // set item as selected to persist highlight
        menuItem.setChecked(true);

        // close drawer when item is tapped
        mDrawerLayout.closeDrawers();

        switch(menuItem.getItemId()) {
            case R.id.nav_pick_event :
                Intent intent = new Intent(this, EventPickingActivity.class);
                startActivity(intent);
                break;

            case R.id.nav_main :
                changeFragment(new EventMainFragment(), true);
                break;

            case R.id.nav_map :
                changeFragment(new EventMapFragment(), true);
                break;

            case R.id.nav_tickets :
                changeFragment(new EventMapFragment(), true);
                break;

            case R.id.nav_schedule :
                changeFragment(new ScheduleParentFragment(), true);
                break;

        }

        return true;
    }

    /**
     * Change the current displayed fragment by a new one.
     * - if the fragment is in backstack, it will pop it
     * - if the fragment is already displayed (trying to change the fragment with the same), it will not do anything
     *
     * @param frag            the new fragment to display
     * @param saveInBackstack if we want the fragment to be in backstack
     */
    private void changeFragment(Fragment frag, boolean saveInBackstack) {
        String backStateName = ((Object) frag).getClass().getName();

        try {
            FragmentManager manager = getSupportFragmentManager();
            boolean fragmentPopped = manager.popBackStackImmediate(backStateName, 0);

            if (!fragmentPopped && manager.findFragmentByTag(backStateName) == null) {
                // fragment not in back stack, create it.
                FragmentTransaction transaction = manager.beginTransaction();

                transaction.replace(R.id.content_frame, frag, backStateName);

                if (saveInBackstack) {
                    Log.d(TAG, "Change Fragment: addToBackTack " + backStateName);
                    transaction.addToBackStack(backStateName);
                } else {
                    Log.d(TAG, "Change Fragment: NO addToBackTack");
                }

                transaction.commit();
            } else {
                // custom effect if fragment is already instanciated
            }
        } catch (IllegalStateException exception) {
            Log.w(TAG,
                    "Unable to commit fragment, could be activity as been killed in background. "
                            + exception.toString()
            );
        }
    }
}
