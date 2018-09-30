package ch.epfl.sweng.eventmanager;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

public class EventPickingActivity extends AppCompatActivity {
    private final String[] EVENTS = {
      "Japan Impact",
      "Sysmic",
      "Souper de section"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_picking);

        // Help text
        TextView helpText = (TextView) findViewById(R.id.help_text);
        helpText.setTypeface(helpText.getTypeface(), Typeface.BOLD);
        helpText.setText("Please select an event to continue.");

        // Event list
        RecyclerView eventList = (RecyclerView) findViewById(R.id.event_list);
        eventList.setHasFixedSize(true);
        LinearLayoutManager eventListLayoutManager = new LinearLayoutManager(this);
        eventList.setLayoutManager(eventListLayoutManager);

        EventListAdapter eventListAdapter = new EventListAdapter(EVENTS);
        eventList.setAdapter(eventListAdapter);
    }

}
