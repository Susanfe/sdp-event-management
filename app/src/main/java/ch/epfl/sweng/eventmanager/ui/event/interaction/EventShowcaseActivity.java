package ch.epfl.sweng.eventmanager.ui.event.interaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModelProviders;
import ch.epfl.sweng.eventmanager.R;
import ch.epfl.sweng.eventmanager.notifications.JoinedEventFeedbackStrategy;
import ch.epfl.sweng.eventmanager.notifications.JoinedEventStrategy;
import ch.epfl.sweng.eventmanager.notifications.NotificationScheduler;
import ch.epfl.sweng.eventmanager.repository.data.Event;
import ch.epfl.sweng.eventmanager.repository.data.EventTicketingConfiguration;
import ch.epfl.sweng.eventmanager.ui.event.interaction.fragments.*;
import ch.epfl.sweng.eventmanager.ui.event.interaction.fragments.map.EventMapEditionFragment;
import ch.epfl.sweng.eventmanager.ui.event.interaction.fragments.map.EventMapFragment;
import ch.epfl.sweng.eventmanager.ui.event.interaction.fragments.schedule.ScheduleParentFragment;
import ch.epfl.sweng.eventmanager.ui.event.interaction.models.*;
import ch.epfl.sweng.eventmanager.ui.event.selection.EventPickingActivity;
import ch.epfl.sweng.eventmanager.ui.settings.SettingsActivity;
import ch.epfl.sweng.eventmanager.ui.ticketing.TicketingManager;
import ch.epfl.sweng.eventmanager.ui.tools.ImageLoader;
import ch.epfl.sweng.eventmanager.users.Role;
import ch.epfl.sweng.eventmanager.users.Session;
import ch.epfl.sweng.eventmanager.viewmodel.ViewModelFactory;
import dagger.android.AndroidInjection;

import java.util.Objects;
import jp.wasabeef.glide.transformations.BlurTransformation;

import javax.inject.Inject;

public class EventShowcaseActivity extends MultiFragmentActivity {
    private static final String TAG = "EventShowcaseActivity";
    private static final int Y_OFFSET_TOAST = 30;

    public enum FragmentType {
        MAIN, MAP, SCHEDULE, NEWS, FORM, EVENT_FEEDBACK, MAP_EDITION
    }

    @Inject
    ViewModelFactory factory;
    @Inject
    TicketingManager ticketingManager;
    @Inject
    Session session;
    @Inject
    ImageLoader loader;

    private EventInteractionModel model;
    private ScheduleViewModel scheduleModel;
    private NewsViewModel newsModel;
    private SpotsModel spotsModel;
    private ZoneModel zonesModel;
    private Fragment eventMainFragment;
    private Fragment newsFragment;
    private Fragment scheduleParentFragment;
    private Fragment eventMapFragment;
    private Fragment eventFormFragment;
    private Fragment eventFeedbackFragment;

    private int eventID;
    private Event event;

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

            this.event = ev;

            if (ev.hasAnImage()) {
                ImageView header = headerView.findViewById(R.id.drawer_header_image);
                loader.loadImageWithSpinner(ev, this, header, new BlurTransformation(3));
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

                if (session.isLoggedIn() && session.isClearedFor(Role.ADMIN, ev)) {
                    MenuItem adminMenuItem = navigationView.getMenu().findItem(R.id.nav_admin);
                    adminMenuItem.setVisible(true);
                }
            });

            // Set displayed fragment only when no other fragment where previously inflated.
            if (savedInstanceState == null)
                    switchFragment(FragmentType.MAIN,true);
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
                adminIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                menuItem.setChecked(false);
                startActivity(adminIntent);
                break;

            case R.id.nav_main:
                switchFragment(FragmentType.MAIN, true);
                break;

            case R.id.nav_map:
                switchFragment(FragmentType.MAP, true);
                break;

            case R.id.nav_news:
                switchFragment(FragmentType.NEWS, true);
                break;

            case R.id.nav_schedule:
                switchFragment(FragmentType.SCHEDULE, true);
                break;

            case R.id.nav_feedback:
                switchFragment(FragmentType.EVENT_FEEDBACK,true);
                break;

            case R.id.nav_scan:
                if(model.getEvent().getValue() != null) {
                    startActivity(ticketingManager.start(model.getEvent().getValue(), this));
                    menuItem.setChecked(false);
                }
                break;

            case R.id.nav_settings:
                Intent intent = new Intent(this, SettingsActivity.class);
                menuItem.setChecked(false);
                startActivity(intent);
                break;

            case R.id.nav_contact:
                switchFragment(FragmentType.FORM, true);
                break;
        }

        return false;
    }

    /**
     * Switch the current fragment to the required one. Instatiate the new fragment if not already instantiated.
     * @param type            type of the fragment to switch to
     * @param saveToBackstack save the fragment in the backstack to access it later on
     */
    public void switchFragment(FragmentType type, boolean saveToBackstack) {
        if (type == null) type = FragmentType.MAIN;
        switch (type) {
            case MAIN:
                if (eventMainFragment == null) {
                    eventMainFragment = EventMainFragment.newInstance();
                }
                changeFragment(eventMainFragment, saveToBackstack);
                break;

            case MAP:
                if (eventMapFragment == null) {
                    eventMapFragment = EventMapFragment.newInstance();
                }
                changeFragment(eventMapFragment, saveToBackstack);
                break;

            case SCHEDULE:
                if (scheduleParentFragment == null) {
                    scheduleParentFragment = ScheduleParentFragment.newInstance();
                }
                changeFragment(scheduleParentFragment, saveToBackstack);
                break;

            case FORM:
                if(eventFormFragment == null) {
                    eventFormFragment = EventFormFragment.newInstance();
                }
                changeFragment(eventFormFragment, saveToBackstack);
                break;

            case NEWS:
                if (newsFragment == null) {
                    newsFragment = NewsFragment.newInstance();
                }
                changeFragment(newsFragment, saveToBackstack);
                break;

            case MAP_EDITION:
                changeFragment(new EventMapEditionFragment(), saveToBackstack);
                break;

            case EVENT_FEEDBACK:
                if(eventFeedbackFragment == null) {
                    eventFeedbackFragment = EventFeedbackFragment.newInstance();
                }
                changeFragment(eventFeedbackFragment,saveToBackstack);
                break;
            default:
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
            if (current instanceof EventMapEditionFragment) navigationView.setCheckedItem(R.id.nav_map);
            if (current instanceof EventFormFragment) navigationView.setCheckedItem(R.id.nav_contact);
            if (current instanceof EventFeedbackFragment) navigationView.setCheckedItem(R.id.nav_feedback);
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
        if (fragment instanceof EventTicketFragment || (fragment instanceof EventMapFragment && !(fragment instanceof EventMapEditionFragment)) ||
                fragment instanceof ScheduleParentFragment || fragment instanceof NewsFragment) {
            switchFragment(FragmentType.MAIN, true);
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

    public Event getEvent() {return event;}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_showcase_activity, menu);
        Switch s = (Switch) menu.findItem(R.id.menu_showcase_activity_join_switch).getActionView();

        model.getEvent().observe(this, ev -> {
            this.model.isJoined(ev).observe(this, s::setChecked);
            s.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    this.model.joinEvent(ev);
                    if(ev.getBeginDateAsDate() != null){
                        NotificationScheduler.scheduleNotification(ev, new JoinedEventStrategy(getApplicationContext()));
                    }
                    if(ev.getEndDateAsDate() != null){
                        NotificationScheduler.scheduleNotification(ev, new JoinedEventFeedbackStrategy(getApplicationContext()));
                    }
                } else {
                    this.model.unjoinEvent(ev);
                    if(ev.getBeginDateAsDate() != null){
                        NotificationScheduler.unscheduleNotification(ev, new JoinedEventStrategy(getApplicationContext()));
                    }
                    if(ev.getEndDateAsDate() != null){
                        NotificationScheduler.unscheduleNotification(ev, new JoinedEventFeedbackStrategy(getApplicationContext()));
                    }
                }
            });

            s.setOnClickListener(buttonView -> {
                if(s.isChecked()){
                    tellUser(String.format("%s %s", getString(R.string.joined_switch_button),  ev.getName()));
                }
                else{
                    tellUser(String.format("%s %s", getString(R.string.unjoined_switch_button),ev.getName()));
                }
            });
        });
        return true;
    }

    private void tellUser(String message) {
        Toast toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.TOP, 0, Y_OFFSET_TOAST);
        toast.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
            default:
                // Action was not consumed
                return false;
        }
    }

    public ImageLoader getLoader() {
        return loader;
    }
}
