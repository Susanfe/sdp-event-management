package ch.epfl.sweng.eventmanager.ui.eventShowcase;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import ch.epfl.sweng.eventmanager.R;
import ch.epfl.sweng.eventmanager.ui.eventSelector.EventPickingActivity;

public class EventActivity extends AppCompatActivity {
    private static final String TAG = "EventActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        Intent intent = getIntent();
        int eventID = intent.getIntExtra(EventPickingActivity.SELECTED_EVENT_ID, 0);
        // TODO: fetch event, update displayed values.
        Log.e(TAG, "Got event ID#" + eventID + " but don't know what to do ;-(");
    }

    public void goToMap(View view) {
        Intent intent = new Intent(this, MapActivity.class);
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
