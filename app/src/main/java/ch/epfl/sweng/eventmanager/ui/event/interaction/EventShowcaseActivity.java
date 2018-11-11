package ch.epfl.sweng.eventmanager.ui.event.interaction;

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

import javax.inject.Inject;

import ch.epfl.sweng.eventmanager.R;
import ch.epfl.sweng.eventmanager.ui.event.interaction.models.EventInteractionModel;
import ch.epfl.sweng.eventmanager.ui.event.selection.EventPickingActivity;
import ch.epfl.sweng.eventmanager.ui.event.interaction.fragments.EventMainFragment;
import ch.epfl.sweng.eventmanager.ui.event.interaction.fragments.EventMapFragment;
import ch.epfl.sweng.eventmanager.ui.event.interaction.fragments.schedule.ScheduleParentFragment;
import ch.epfl.sweng.eventmanager.ui.event.interaction.models.ScheduleViewModel;
import ch.epfl.sweng.eventmanager.ui.event.interaction.models.SpotsModel;
import ch.epfl.sweng.eventmanager.users.Role;
import ch.epfl.sweng.eventmanager.users.Session;
import ch.epfl.sweng.eventmanager.viewmodel.ViewModelFactory;
import dagger.android.AndroidInjection;

public class EventShowcaseActivity extends MultiFragmentActivity
        implements NavigationView.OnNavigationItemSelectedListener{
    private static final String TAG = "EventShowcaseActivity";

    @Inject
    ViewModelFactory factory;

    private EventInteractionModel model;
    private ScheduleViewModel scheduleModel;
    private SpotsModel spotsModel;
    private DrawerLayout mDrawerLayout;
    private int eventID;

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
        eventID = intent.getIntExtra(EventPickingActivity.SELECTED_EVENT_ID, -1);
        if (eventID <= 0) { // Suppose that negative or null event ID are invalids
            Log.e(TAG, "Got invalid event ID#" + eventID + ".");
        } else {
            this.model = ViewModelProviders.of(this, factory).get(EventInteractionModel.class);
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

                        // Set window title
                        setTitle("Admin: " + ev.getName());

                        // Only display the admin button is the user is cleared for ADMIN rights
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
                Intent pickingIntent = new Intent(this, EventPickingActivity.class);
                startActivity(pickingIntent);
                break;

            case R.id.nav_admin :
                Intent adminIntent = new Intent(this, EventAdministrationActivity.class);
                adminIntent.putExtra(EventPickingActivity.SELECTED_EVENT_ID, eventID);
                startActivity(adminIntent);
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
}
