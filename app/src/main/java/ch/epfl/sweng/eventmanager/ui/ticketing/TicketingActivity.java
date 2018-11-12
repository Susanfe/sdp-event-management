package ch.epfl.sweng.eventmanager.ui.ticketing;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import ch.epfl.sweng.eventmanager.repository.data.EventTicketingConfiguration;
import ch.epfl.sweng.eventmanager.ticketing.TicketingService;
import ch.epfl.sweng.eventmanager.ticketing.TicketingServiceManager;
import ch.epfl.sweng.eventmanager.ui.eventSelector.EventPickingActivity;
import ch.epfl.sweng.eventmanager.ui.eventShowcase.EventShowcaseActivity;
import dagger.android.AndroidInjection;

import javax.inject.Inject;

/**
 * @author Louis Vialar
 */
public abstract class TicketingActivity extends AppCompatActivity {
    static final String SELECTED_EVENT_ID = "ch.epfl.sweng.SELECTED_EVENT_ID";
    static final String TICKETING_CONFIGURATION = "ch.epfl.sweng.TICKETING_CONFIGURATION";
    private static final String TAG = "TicketingActivity";
    @Inject
    protected TicketingServiceManager manager;
    TicketingService service;
    private int eventId;
    private EventTicketingConfiguration configuration;

    static Class<? extends TicketingActivity> getNextActivityForState(TicketingService service) {
        if (service.isLoggedIn()) {
            if (service.hasMultipleConfigurations()) {
                return TicketingConfigurationPickerActivity.class;
            } else {
                return TicketingScanActivity.class;
            }
        } else {
            return TicketingLoginActivity.class;
        }
    }

    Intent switchActivity(Class<? extends TicketingActivity> next) {
        Intent openIntent = new Intent(this, next);
        openIntent.putExtra(SELECTED_EVENT_ID, eventId);
        openIntent.putExtra(TICKETING_CONFIGURATION, configuration);
        return openIntent;
    }

    Intent backToShowcase() {
        Intent openIntent = new Intent(this, EventShowcaseActivity.class);
        openIntent.putExtra(EventPickingActivity.SELECTED_EVENT_ID, eventId);
        return openIntent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        AndroidInjection.inject(this);

        super.onCreate(savedInstanceState);

        // Get model
        Intent intent = getIntent();
        this.eventId = intent.getIntExtra(SELECTED_EVENT_ID, -1);
        this.configuration = intent.getParcelableExtra(TICKETING_CONFIGURATION);

        if (eventId <= 0) { // Suppose that negative or null event ID are invalids
            Log.e(TAG, "Got invalid event ID#" + eventId + ".");
        } else if (configuration == null) {
            Log.e(TAG, "Got no configuration for event " + eventId);
        } else {
            this.service = manager.getService(this.eventId, this.configuration, this);
        }
    }
}
