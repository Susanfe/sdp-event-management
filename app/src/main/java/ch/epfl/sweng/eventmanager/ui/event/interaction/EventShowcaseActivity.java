package ch.epfl.sweng.eventmanager.ui.event.interaction;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import javax.inject.Inject;

import ch.epfl.sweng.eventmanager.R;
import ch.epfl.sweng.eventmanager.ui.event.interaction.fragments.EventMainFragment;
import ch.epfl.sweng.eventmanager.ui.event.interaction.fragments.EventMapFragment;
import ch.epfl.sweng.eventmanager.ui.event.interaction.fragments.EventTicketFragment;
import ch.epfl.sweng.eventmanager.ui.event.interaction.fragments.NewsFragment;
import ch.epfl.sweng.eventmanager.ui.event.interaction.fragments.schedule.ScheduleParentFragment;
import ch.epfl.sweng.eventmanager.ui.event.interaction.models.EventInteractionModel;
import ch.epfl.sweng.eventmanager.ui.event.interaction.models.NewsViewModel;
import ch.epfl.sweng.eventmanager.ui.event.interaction.models.ScheduleViewModel;
import ch.epfl.sweng.eventmanager.ui.event.interaction.models.SpotsModel;
import ch.epfl.sweng.eventmanager.ui.event.selection.EventPickingActivity;
import ch.epfl.sweng.eventmanager.users.Role;
import ch.epfl.sweng.eventmanager.users.Session;
import ch.epfl.sweng.eventmanager.viewmodel.ViewModelFactory;
import dagger.android.AndroidInjection;

public class EventShowcaseActivity extends MultiFragmentActivity {
    private static final String TAG = "EventShowcaseActivity";

    @Inject
    ViewModelFactory factory;

    private EventInteractionModel model;
    private ScheduleViewModel scheduleModel;
    private NewsViewModel newsModel;
    private SpotsModel spotsModel;

    private int eventID;

    private void initModels() {
        this.model = ViewModelProviders.of(this, factory).get(EventInteractionModel.class);
        this.model.init(eventID);

        this.scheduleModel = ViewModelProviders.of(this, factory).get(ScheduleViewModel.class);
        this.scheduleModel.init(eventID);

        this.newsModel = ViewModelProviders.of(this, factory).get(NewsViewModel.class);
        this.newsModel.init(eventID);

        this.spotsModel = ViewModelProviders.of(this, factory).get(SpotsModel.class);
        this.spotsModel.init(eventID);
    }

    private void setupHeader() {
        // Set window title and configure header
        View headerView = navigationView.getHeaderView(0);
        TextView drawer_header_text = headerView.findViewById(R.id.drawer_header_text);
        model.getEvent().observe(this, ev -> {
            if (ev == null) {
                return;
            }

            drawer_header_text.setText(ev.getName());
            setTitle(ev.getName());
        });

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_showcase);
        initializeSharedUI();

        // Fetch event from passed ID
        Intent intent = getIntent();
        eventID = intent.getIntExtra(EventPickingActivity.SELECTED_EVENT_ID, -1);
        if (eventID <= 0) { // Suppose that negative or null event ID are invalids
            Log.e(TAG, "Got invalid event ID#" + eventID + ".");
        } else {
            this.eventID = eventID;
            this.initModels();
            this.setupHeader();

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

            // Set displayed fragment
            changeFragment(new EventMainFragment(), true);
        }

        // Handle drawer events
        navigationView.setNavigationItemSelectedListener(this);

        //Handle highlighting in drawer menu according to currently displayed fragment
        setHighlightedItemInNavigationDrawer();
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
                pickingIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(pickingIntent);
                break;

            case R.id.nav_admin :
                Intent adminIntent = new Intent(this, EventAdministrationActivity.class);
                adminIntent.putExtra(EventPickingActivity.SELECTED_EVENT_ID, eventID);
                startActivity(adminIntent);

            case R.id.nav_main:
                changeFragment(new EventMainFragment(), true);
                break;

            case R.id.nav_map:
                changeFragment(new EventMapFragment(), true);
                break;

            case R.id.nav_tickets:
                changeFragment(new EventMapFragment(), true);
                break;

            case R.id.nav_news:
                changeFragment(new NewsFragment(), true);
                break;

            case R.id.nav_schedule:
                changeFragment(new ScheduleParentFragment(), true);
                break;
        }

        return true;
    }

    /**
     * Sets the highlighted item in the drawer according to the fragment which is displayed to the user
     */
    private void setHighlightedItemInNavigationDrawer() {
        this.getSupportFragmentManager().addOnBackStackChangedListener(
                () -> {
                    Fragment current = getCurrentFragment();
                    if (current instanceof EventMainFragment)
                        navigationView.setCheckedItem(R.id.nav_main);
                    if (current instanceof NewsFragment)
                        navigationView.setCheckedItem(R.id.nav_news);
                    if (current instanceof EventMapFragment)
                        navigationView.setCheckedItem(R.id.nav_map);
                    if (current instanceof ScheduleParentFragment)
                        navigationView.setCheckedItem(R.id.nav_schedule);
                    if (current instanceof EventTicketFragment)
                        navigationView.setCheckedItem(R.id.nav_tickets);
                }
        );
    }

    /**
     * @return currentFragment
     */
    private Fragment getCurrentFragment() {
        return getSupportFragmentManager().findFragmentById(R.id.content_frame);
    }

    /**
     * Handles back button
     */
    @Override
    public void onBackPressed() {
        Fragment fragment = getCurrentFragment();
        if (fragment instanceof EventTicketFragment || fragment instanceof EventMapFragment || fragment instanceof ScheduleParentFragment || fragment instanceof NewsFragment) {
            changeFragment(new EventMainFragment(), true);
        } else {
            int fragments = getSupportFragmentManager().getBackStackEntryCount();
            if (fragments == 1) {
                finish();
            } else {
                if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
                    getSupportFragmentManager().popBackStack();
                } else {
                    super.onBackPressed();
                }
            }
        }
    }

    @Override
    public void setTitle(CharSequence title) {
        toolbar.setTitle(title);
    }

    public int getEventID() {
        return eventID;
    }
}
