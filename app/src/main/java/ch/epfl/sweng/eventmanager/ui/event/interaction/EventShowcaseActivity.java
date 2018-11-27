package ch.epfl.sweng.eventmanager.ui.event.interaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModelProviders;
import ch.epfl.sweng.eventmanager.R;
import ch.epfl.sweng.eventmanager.repository.data.Event;
import ch.epfl.sweng.eventmanager.repository.data.EventTicketingConfiguration;
import ch.epfl.sweng.eventmanager.ui.event.interaction.fragments.*;
import ch.epfl.sweng.eventmanager.ui.event.interaction.fragments.schedule.ScheduleParentFragment;
import ch.epfl.sweng.eventmanager.ui.event.interaction.fragments.settings.SettingsActivity;
import ch.epfl.sweng.eventmanager.ui.event.interaction.models.*;
import ch.epfl.sweng.eventmanager.ui.event.selection.EventPickingActivity;
import ch.epfl.sweng.eventmanager.ui.ticketing.TicketingManager;
import ch.epfl.sweng.eventmanager.users.Role;
import ch.epfl.sweng.eventmanager.users.Session;
import ch.epfl.sweng.eventmanager.viewmodel.ViewModelFactory;
import dagger.android.AndroidInjection;

import javax.inject.Inject;

public class EventShowcaseActivity extends MultiFragmentActivity {
    private static final String TAG = "EventShowcaseActivity";

    @Inject
    ViewModelFactory factory;
    @Inject
    TicketingManager ticketingManager;

    private EventInteractionModel model;
    private ScheduleViewModel scheduleModel;
    private NewsViewModel newsModel;
    private SpotsModel spotsModel;
    private ZoneModel zonesModel;
    private Fragment eventMainFragment;
    private Fragment newsFragment;
    private Fragment scheduleParentFragment;

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

        this.zonesModel = ViewModelProviders.of(this, factory).get(ZoneModel.class);
        this.zonesModel.init(eventID);
    }

    private void setupMenu() {
        LiveData<EventTicketingConfiguration> data = Transformations.map(model.getEvent(),
                Event::getTicketingConfiguration);
        data.observe(this, d -> {
            MenuItem item = navigationView.getMenu().findItem(R.id.nav_scan);
            if (d != null) {
                Log.i(TAG, "Got a ticketing configuration, setting button visible");
                item.setVisible(true);
            } else {
                Log.i(TAG, "Got no ticketing configuration, setting button invisible");
                item.setVisible(false);
            }
        });
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
            this.initModels();
            this.setupHeader();
            this.setupMenu();

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

            // Set displayed fragment only when no other fragment where previously inflated.
            if (savedInstanceState == null) {
                eventMainFragment = new EventMainFragment();
                changeFragment(eventMainFragment, true);
            }
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

        switch (menuItem.getItemId()) {
            case R.id.nav_pick_event:
                Intent pickingIntent = new Intent(this, EventPickingActivity.class);
                pickingIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(pickingIntent);
                break;

            case R.id.nav_admin:
                Intent adminIntent = new Intent(this, EventAdministrationActivity.class);
                adminIntent.putExtra(EventPickingActivity.SELECTED_EVENT_ID, eventID);
                startActivity(adminIntent);
                break;

            case R.id.nav_main:
                callChangeFragment(FragmentType.MAIN, true);
                break;

            case R.id.nav_map:
                callChangeFragment(FragmentType.MAP, true);
                break;

            case R.id.nav_tickets:
                callChangeFragment(null, true);
                break;

            case R.id.nav_news:
                callChangeFragment(FragmentType.NEWS, true);
                break;

            case R.id.nav_schedule:
                callChangeFragment(FragmentType.SCHEDULE, true);
                break;

            case R.id.nav_scan:
                // TODO Handle null pointer exception
                startActivity(ticketingManager.start(model.getEvent().getValue(), this));
                break;

            case R.id.nav_settings:
                Intent intent = new Intent(this,SettingsActivity.class);
                startActivity(intent);
                break;
        }

        return true;
    }

    /**
     * Prepares the call to changeFragment by verifying if an existing fragment was stored and can
     * be reused.
     *
     * @param type            type of the fragment to switch to
     * @param saveToBackstack save the fragment in the backstack to access it later on
     */
    public void callChangeFragment(FragmentType type, boolean saveToBackstack) {
        if (type == null) type = FragmentType.MAIN;
        switch (type) {
            case MAIN:
                if (eventMainFragment == null) eventMainFragment = new EventMainFragment();
                changeFragment(eventMainFragment, saveToBackstack);
                break;

            case MAP:
                changeFragment(new EventMapFragment(), saveToBackstack);
                break;

            case SCHEDULE:
                if (scheduleParentFragment == null) scheduleParentFragment = new ScheduleParentFragment();
                changeFragment(scheduleParentFragment, saveToBackstack);
                break;

            case FORM:
                changeFragment(new EventFormFragment(), saveToBackstack);
                break;

            case NEWS:
                if (newsFragment == null) newsFragment = new NewsFragment();
                changeFragment(newsFragment, saveToBackstack);
                break;
            default:
                changeFragment(new EventMainFragment(), saveToBackstack);
                break;
        }
    }

    /**
     * Sets the highlighted item in the drawer according to the fragment which is displayed to the user
     */
    private void setHighlightedItemInNavigationDrawer() {
        this.getSupportFragmentManager().addOnBackStackChangedListener(() -> {
            Fragment current = getCurrentFragment();
            if (current instanceof EventMainFragment) navigationView.setCheckedItem(R.id.nav_main);
            if (current instanceof NewsFragment) navigationView.setCheckedItem(R.id.nav_news);
            if (current instanceof EventMapFragment) navigationView.setCheckedItem(R.id.nav_map);
            if (current instanceof ScheduleParentFragment) navigationView.setCheckedItem(R.id.nav_schedule);
            if (current instanceof EventTicketFragment) navigationView.setCheckedItem(R.id.nav_tickets);
        });
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
            callChangeFragment(FragmentType.MAIN, true);
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

    public enum FragmentType {
        MAIN, MAP, SCHEDULE, NEWS, FORM
    }
}
