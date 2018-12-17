package ch.epfl.sweng.eventmanager.ui.event.interaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProviders;
import ch.epfl.sweng.eventmanager.R;
import ch.epfl.sweng.eventmanager.ui.event.interaction.fragments.EventAdminMainFragment;
import ch.epfl.sweng.eventmanager.ui.event.interaction.fragments.SendNewsFragment;
import ch.epfl.sweng.eventmanager.ui.event.interaction.fragments.schedule.AdminScheduleParentFragment;
import ch.epfl.sweng.eventmanager.ui.event.interaction.fragments.SendNotificationFragment;
import ch.epfl.sweng.eventmanager.ui.event.interaction.fragments.user.EventUserManagementFragment;
import ch.epfl.sweng.eventmanager.ui.event.interaction.models.EventInteractionModel;
import ch.epfl.sweng.eventmanager.ui.event.interaction.models.NewsViewModel;
import ch.epfl.sweng.eventmanager.ui.event.interaction.models.ScheduleViewModel;
import ch.epfl.sweng.eventmanager.ui.event.selection.EventPickingActivity;
import ch.epfl.sweng.eventmanager.viewmodel.ViewModelFactory;
import dagger.android.AndroidInjection;

import javax.inject.Inject;

public class EventAdministrationActivity extends MultiFragmentActivity {
    private static final String TAG = "EventAdministration";

    @Inject
    ViewModelFactory factory;

    private NewsViewModel newsModel;
    private ScheduleViewModel scheduleModel;

    private void initModels() {
        model = ViewModelProviders.of(this, factory).get(EventInteractionModel.class);
        model.init(eventID);

        newsModel = ViewModelProviders.of(this, factory).get(NewsViewModel.class);
        newsModel.init(eventID);

        scheduleModel = ViewModelProviders.of(this, factory).get(ScheduleViewModel.class);
        scheduleModel.init(eventID);
    }

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
            this.initModels();
            super.setupHeader();

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
            changeFragment(new EventAdminMainFragment(), true);
            navigationView.setCheckedItem(R.id.nav_admin_main);
        }

        // Handle drawer events
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        // close drawer when item is tapped
        mDrawerLayout.closeDrawers();

        switch(menuItem.getItemId()) {
            case R.id.nav_showcase :
                Intent showcaseIntent = new Intent(this, EventShowcaseActivity.class);
                showcaseIntent.putExtra(EventPickingActivity.SELECTED_EVENT_ID, eventID);
                showcaseIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(showcaseIntent);
                menuItem.setChecked(false);
                break;

            case R.id.nav_admin_main :
                changeFragment(new EventAdminMainFragment(), true);
                menuItem.setChecked(true);
                break;

            case R.id.nav_user_management :
                changeFragment(new EventUserManagementFragment(), true);
                menuItem.setChecked(true);
                break;

            case R.id.nav_send_news :
                changeFragment(new SendNewsFragment(), true);
                menuItem.setChecked(true);
                break;

            case R.id.nav_send_notification :
                changeFragment(new SendNotificationFragment(), true);
                menuItem.setChecked(true);
                break;

            case R.id.nav_edit_schedule :
                changeFragment(AdminScheduleParentFragment.newInstance(), true);
                menuItem.setChecked(true);
                break;

            case R.id.nav_edit_event :
                Intent editIntent = new Intent(this, EventCreateActivity.class);
                editIntent.putExtra(EventPickingActivity.SELECTED_EVENT_ID, eventID);
                startActivity(editIntent);
                menuItem.setChecked(false);
                break;
        }

        return false;
    }
}
