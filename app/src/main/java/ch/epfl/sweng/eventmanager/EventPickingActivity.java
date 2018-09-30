package ch.epfl.sweng.eventmanager;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;

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
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        RecyclerView eventList = (RecyclerView) findViewById(R.id.eventList);
        eventList.setHasFixedSize(true);
        LinearLayoutManager eventListLayoutManager = new LinearLayoutManager(this);
        eventList.setLayoutManager(eventListLayoutManager);

        EventListAdapter eventListAdapter = new EventListAdapter(EVENTS);
        eventList.setAdapter(eventListAdapter);
    }

}
