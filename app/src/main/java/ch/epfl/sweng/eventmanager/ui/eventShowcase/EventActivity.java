package ch.epfl.sweng.eventmanager.ui.eventShowcase;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import ch.epfl.sweng.eventmanager.R;
import ch.epfl.sweng.eventmanager.ui.eventSelector.EventPickingActivity;
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
        // TODO: fetch event, update displayed values.
        if (eventID <= 0) { // Suppose that negative or null event ID are invalids
            // TODO: find a way to pass the event ID between the different views
            Log.e(TAG, "Got invalid event ID#" + eventID + ".");
        } else {
            this.model = ViewModelProviders.of(this, factory).get(EventShowcaseModel.class);
            this.model.init(eventID);
            this.model.getEvent().observe(this, ev -> {
                Log.v(TAG, "Got event ID#" + eventID + " -> event " + ev);
                TextView eventDescription = (TextView) findViewById(R.id.event_description);
                eventDescription.setText(ev.getDescription());
            });
        }
    }

    public void goToMap(View view) {
        Intent intent = new Intent(this, MapsActivity.class);
        intent.putExtra(EventPickingActivity.SELECTED_EVENT_ID, eventID);
        startActivity(intent);
    }

    public void goToSchedule(View view) {
        Intent intent = new Intent(this, ScheduleActivity.class);
        startActivity(intent);
    }

    public void goToBuyingTicket(View view) {
        Intent intent = new Intent(this, TicketActivity.class);
        startActivity(intent);
    }

}
