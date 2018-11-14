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
import ch.epfl.sweng.eventmanager.ui.event.interaction.fragments.EventUserManagementFragment;
import ch.epfl.sweng.eventmanager.ui.event.interaction.models.EventInteractionModel;
import ch.epfl.sweng.eventmanager.ui.event.selection.EventPickingActivity;
import ch.epfl.sweng.eventmanager.ui.event.interaction.fragments.schedule.ScheduleParentFragment;
import ch.epfl.sweng.eventmanager.viewmodel.ViewModelFactory;
import dagger.android.AndroidInjection;

public class EventAdministrationActivity extends MultiFragmentActivity {
    private static final String TAG = "EventAdministration";

    @Inject
    ViewModelFactory factory;

    private EventInteractionModel model;
    private int eventID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_administration);
        initializeSharedUI();

        // Fetch event from passed ID
        Intent intent = getIntent();
        eventID = intent.getIntExtra(EventPickingActivity.SELECTED_EVENT_ID, -1);
        if (eventID <= 0) { // Suppose that negative or null event ID are invalids
            Log.e(TAG, "Got invalid event ID#" + eventID + ".");
        } else {
            this.model = ViewModelProviders.of(this, factory).get(EventInteractionModel.class);
            this.model.init(eventID);

            // Only display admin button if the user is at least staff
            model.getEvent().observe(this, ev -> {
                if (ev == null) {
                    Log.e(TAG, "Got null model from parent activity");
                    return;
                }

                // Set window title
                setTitle("Admin: " + ev.getName());
            });

            // Set default administration fragment
            changeFragment(new EventUserManagementFragment(), true);
        }

        // Handle drawer events
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        // set item as selected to persist highlight
        menuItem.setChecked(true);

        // close drawer when item is tapped
        mDrawerLayout.closeDrawers();

        switch(menuItem.getItemId()) {
            case R.id.nav_showcase :
                Intent showcaseIntent = new Intent(this, EventShowcaseActivity.class);
                showcaseIntent.putExtra(EventPickingActivity.SELECTED_EVENT_ID, eventID);
                startActivity(showcaseIntent);
                break;

            case R.id.nav_schedule :
                changeFragment(new ScheduleParentFragment(), true);
                break;
        }

        return true;
    }
}
