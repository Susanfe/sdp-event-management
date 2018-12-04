package ch.epfl.sweng.eventmanager.ui.event.interaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import ch.epfl.sweng.eventmanager.R;
import ch.epfl.sweng.eventmanager.ui.event.interaction.fragments.EventFeedbackFragment;
import ch.epfl.sweng.eventmanager.ui.event.interaction.fragments.EventFormFragment;
import ch.epfl.sweng.eventmanager.ui.event.interaction.fragments.EventMainFragment;
import ch.epfl.sweng.eventmanager.ui.event.interaction.fragments.EventMapFragment;
import ch.epfl.sweng.eventmanager.ui.event.interaction.fragments.EventTicketFragment;
import ch.epfl.sweng.eventmanager.ui.event.interaction.fragments.NewsFragment;
import ch.epfl.sweng.eventmanager.ui.event.interaction.fragments.schedule.ScheduleParentFragment;
import ch.epfl.sweng.eventmanager.ui.event.interaction.models.EventInteractionModel;
import ch.epfl.sweng.eventmanager.ui.event.interaction.models.NewsViewModel;
import ch.epfl.sweng.eventmanager.ui.event.interaction.models.ScheduleViewModel;
import ch.epfl.sweng.eventmanager.ui.event.interaction.models.SpotsModel;
import ch.epfl.sweng.eventmanager.ui.event.interaction.models.ZoneModel;
import ch.epfl.sweng.eventmanager.ui.event.selection.EventPickingActivity;
import ch.epfl.sweng.eventmanager.ui.settings.SettingsActivity;
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
    private ZoneModel zonesModel;
    private Fragment eventMainFragment;
    private Fragment newsFragment;
    private Fragment scheduleParentFragment;

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
                String fragment = intent.getStringExtra("fragment");
                if (fragment!= null && fragment.equals("feedback"))
                    changeFragment(new EventFeedbackFragment(), true);
                else {
                    eventMainFragment = new EventMainFragment();
                    changeFragment(eventMainFragment, true);
                }
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

            case R.id.nav_news:
                callChangeFragment(FragmentType.NEWS, true);
                break;

            case R.id.nav_schedule:
                callChangeFragment(FragmentType.SCHEDULE, true);
                break;

            case R.id.nav_feedback:
                changeFragment(new EventFeedbackFragment(), true);
                break;

            case R.id.nav_settings:
                Intent intent = new Intent(this, SettingsActivity.class);
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
