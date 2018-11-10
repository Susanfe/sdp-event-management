package ch.epfl.sweng.eventmanager.ui.ticketing;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import ch.epfl.sweng.eventmanager.repository.data.Event;
import ch.epfl.sweng.eventmanager.repository.data.EventTicketingConfiguration;
import ch.epfl.sweng.eventmanager.ticketing.TicketingService;
import ch.epfl.sweng.eventmanager.ui.eventSelector.EventPickingActivity;
import ch.epfl.sweng.eventmanager.ui.eventShowcase.EventShowcaseActivity;

/**
 * @author Louis Vialar
 */
public abstract class TicketingActivity extends AppCompatActivity {
    private static final String TAG = "TicketingActivity";
    public static final String SELECTED_EVENT_ID = "ch.epfl.sweng.SELECTED_EVENT_ID";
    public static final String TICKETING_CONFIGURATION = "ch.epfl.sweng.TICKETING_CONFIGURATION";

    private int eventId;
    private EventTicketingConfiguration configuration;
    protected TicketingService service;

    public static Intent start(Event event, Context context) {
        if (event.getTicketingConfiguration() == null) {
            throw new UnsupportedOperationException("this event doesn't support ticketing");
        }

        TicketingService service = TicketingService.getService(event.getTicketingConfiguration(), context);

        Intent openIntent = new Intent(context, getNextActivityForState(service));
        openIntent.putExtra(SELECTED_EVENT_ID, event.getId());
        openIntent.putExtra(TICKETING_CONFIGURATION, event.getTicketingConfiguration());
        return openIntent;
    }

    protected static Class<? extends TicketingActivity> getNextActivityForState(TicketingService service) {
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

    protected Intent switchActivity(Class<? extends TicketingActivity> next) {
        Intent openIntent = new Intent(this, next);
        openIntent.putExtra(SELECTED_EVENT_ID, eventId);
        openIntent.putExtra(TICKETING_CONFIGURATION, configuration);
        return openIntent;
    }

    protected Intent backToShowcase() {
        Intent openIntent = new Intent(this, EventShowcaseActivity.class);
        openIntent.putExtra(EventPickingActivity.SELECTED_EVENT_ID, eventId);
        return openIntent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
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
            this.service = TicketingService.getService(this.configuration, this);
        }
    }
}
