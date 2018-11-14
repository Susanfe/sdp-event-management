package ch.epfl.sweng.eventmanager.ui.eventAdministration;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import ch.epfl.sweng.eventmanager.R;

public class EventAdministrationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_administration);

        // Set activity title (displayed in action bar)
        setTitle("Event Administration");
    }
}
