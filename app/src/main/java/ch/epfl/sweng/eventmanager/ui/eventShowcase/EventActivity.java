package ch.epfl.sweng.eventmanager.ui.eventShowcase;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;

import ch.epfl.sweng.eventmanager.R;
import ch.epfl.sweng.eventmanager.ui.eventSelector.EventPickingActivity;
import ch.epfl.sweng.eventmanager.ui.schedule.ScheduleActivity;
import ch.epfl.sweng.eventmanager.viewmodel.ViewModelFactory;
import dagger.android.AndroidInjection;

import javax.inject.Inject;

public class EventActivity extends AppCompatActivity {
    private static final String TAG = "EventActivity";

    @Inject
    ViewModelFactory factory;

    private EventShowcaseModel model;
    private int eventID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        Intent intent = getIntent();
        eventID = intent.getIntExtra(EventPickingActivity.SELECTED_EVENT_ID, -1);
        if (eventID <= 0) { // Suppose that negative or null event ID are invalids
            // TODO: find a way to pass the event ID between the different views
            Log.e(TAG, "Got invalid event ID#" + eventID + ".");
        } else {
            this.model = ViewModelProviders.of(this, factory).get(EventShowcaseModel.class);
            this.model.init(eventID);
            this.model.getEvent().observe(this, ev -> {
                if (ev == null)
                    return;

                // Set the window's title
                setTitle(ev.getName());

                // Populate the event's details
                TextView eventDescription = (TextView) findViewById(R.id.event_description);
                eventDescription.setText(ev.getDescription());

                // Binds the 'join event' switch to the database
                Switch joinEventSwitch = (Switch) findViewById(R.id.join_event_switch);
                // State of the switch depends on if the user joined the event
                this.model.isJoined(ev).observe(this, joinEventSwitch::setChecked);
                joinEventSwitch.setOnClickListener(view -> {
                    if (joinEventSwitch.isChecked()) this.model.joinEvent(ev);
                    else this.model.unjoinEvent(ev);
                });
            });
        }
    }

    public void goToMap(View view) {
        Intent intent = new Intent(this, MapActivity.class);
        startActivity(intent);
    }

    public void goToSchedule(View view) {
        Intent intent = new Intent(this, ScheduleActivity.class);
        intent.putExtra(EventPickingActivity.SELECTED_EVENT_ID, eventID);
        startActivity(intent);
    }

    public void goToBuyingTicket(View view) {
        Intent intent = new Intent(this, TicketActivity.class);
        startActivity(intent);
    }

}
