package ch.epfl.sweng.eventmanager.ui.eventSelector;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import ch.epfl.sweng.eventmanager.ui.eventShowcase.EventActivity;
import ch.epfl.sweng.eventmanager.R;
import ch.epfl.sweng.eventmanager.viewmodel.DaggerViewModelFactory;
import dagger.android.AndroidInjection;

import javax.inject.Inject;

public class EventPickingActivity extends AppCompatActivity {
    private EventListModel model;
    public static final String SELECTED_EVENT_ID = "ch.epfl.sweng.SELECTED_EVENT_ID";
    @Inject
    DaggerViewModelFactory factory;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        AndroidInjection.inject(this);

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
        DividerItemDecoration eventListDividerItemDecoration = new DividerItemDecoration(
                eventList.getContext(),
                eventListLayoutManager.getOrientation()
        );
        eventList.addItemDecoration(eventListDividerItemDecoration);

        this.model = ViewModelProviders.of(this, factory).get(EventListModel.class);
        this.model.init();
        this.model.getEvents().observe(this, list -> {
            EventListAdapter eventListAdapter = new EventListAdapter(list);
            eventList.setAdapter(eventListAdapter);
        });

    }
}
