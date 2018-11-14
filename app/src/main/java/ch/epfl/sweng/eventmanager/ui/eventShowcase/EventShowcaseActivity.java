package ch.epfl.sweng.eventmanager.ui.eventShowcase;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import ch.epfl.sweng.eventmanager.R;
import ch.epfl.sweng.eventmanager.repository.data.Event;
import ch.epfl.sweng.eventmanager.repository.data.EventTicketingConfiguration;
import ch.epfl.sweng.eventmanager.ui.eventAdministration.EventAdministrationActivity;
import ch.epfl.sweng.eventmanager.ui.eventSelector.EventPickingActivity;
import ch.epfl.sweng.eventmanager.ui.eventShowcase.fragments.EventMainFragment;
import ch.epfl.sweng.eventmanager.ui.eventShowcase.fragments.EventMapFragment;
import ch.epfl.sweng.eventmanager.ui.eventShowcase.fragments.EventTicketFragment;
import ch.epfl.sweng.eventmanager.ui.eventShowcase.fragments.NewsFragment;
import ch.epfl.sweng.eventmanager.ui.eventShowcase.fragments.schedule.ScheduleParentFragment;
import ch.epfl.sweng.eventmanager.ui.eventShowcase.models.EventShowcaseModel;
import ch.epfl.sweng.eventmanager.ui.eventShowcase.models.NewsViewModel;
import ch.epfl.sweng.eventmanager.ui.eventShowcase.models.ScheduleViewModel;
import ch.epfl.sweng.eventmanager.ui.eventShowcase.models.SpotsModel;
import ch.epfl.sweng.eventmanager.ui.ticketing.TicketingActivity;
import ch.epfl.sweng.eventmanager.ui.ticketing.TicketingManager;
import ch.epfl.sweng.eventmanager.users.Role;
import ch.epfl.sweng.eventmanager.users.Session;
import ch.epfl.sweng.eventmanager.viewmodel.ViewModelFactory;
import dagger.android.AndroidInjection;

import javax.inject.Inject;

public class EventShowcaseActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = "EventShowcaseActivity";

    @Inject
    ViewModelFactory factory;
    @Inject
    TicketingManager ticketingManager;

    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    @BindView(R.id.nav_view)
    NavigationView navigationView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private EventShowcaseModel model;
    private ScheduleViewModel scheduleModel;
    private NewsViewModel newsModel;
    private SpotsModel spotsModel;

    private int eventID;

    private void initModels() {
        this.model = ViewModelProviders.of(this, factory).get(EventShowcaseModel.class);
        this.model.init(eventID);

        this.scheduleModel = ViewModelProviders.of(this, factory).get(ScheduleViewModel.class);
        this.scheduleModel.init(eventID);

        this.newsModel = ViewModelProviders.of(this, factory).get(NewsViewModel.class);
        this.newsModel.init(eventID);

        this.spotsModel = ViewModelProviders.of(this, factory).get(SpotsModel.class);
        this.spotsModel.init(eventID);
    }

    private void setupMenu() {
        LiveData<EventTicketingConfiguration> data = Transformations.map(model.getEvent(), Event::getTicketingConfiguration);
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
        mDrawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = mDrawerLayout.findViewById(R.id.nav_view);
        ButterKnife.bind(this);

        // Set toolbar as action bar
        setSupportActionBar(toolbar);

        // Add drawer button to the action bar
        if (getSupportActionBar() != null) {
            ActionBar actionbar = getSupportActionBar();
            actionbar.setDisplayHomeAsUpEnabled(true);
            actionbar.setHomeAsUpIndicator(R.drawable.menu_customized_solor);
        }

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

            // Set displayed fragment
            changeFragment(new EventMainFragment(), true);
        }

        // Handle drawer events
        navigationView.setNavigationItemSelectedListener(this);

        //Handle highlighting in drawer menu according to currently displayed fragment
        setHighlightedItemInNavigationDrawer();
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

            case R.id.nav_scan:
                startActivity(ticketingManager.start(model.getEvent().getValue(), this));
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
     * Change the current displayed fragment by a new one.
     * - if the fragment is in backstack, it will pop it
     * - if the fragment is already displayed (trying to change the fragment with the same), it will not do anything
     *
     * @param frag            the new fragment to display
     * @param saveInBackstack if we want the fragment to be in backstack
     */
    public void changeFragment(Fragment frag, boolean saveInBackstack) {
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
                // TODO custom effect if fragment is already instanciated
            }
        } catch (IllegalStateException exception) {
            Log.w(TAG,
                    "Unable to commit fragment, could be activity as been killed in background. "
                            + exception.toString()
            );
        }
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
