package ch.epfl.sweng.eventmanager.ui.eventSelector;

import android.arch.lifecycle.ViewModelProviders;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import ch.epfl.sweng.eventmanager.R;
import ch.epfl.sweng.eventmanager.viewmodel.ViewModelFactory;
import dagger.android.AndroidInjection;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collections;

public class EventPickingActivity extends AppCompatActivity {
    private EventPickingModel model;
    public static final String SELECTED_EVENT_ID = "ch.epfl.sweng.SELECTED_EVENT_ID";
    @Inject
    ViewModelFactory factory;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        AndroidInjection.inject(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_picking);

        // Help text
        // Both invisible by default
        TextView joinedHelpText = (TextView) findViewById(R.id.joined_help_text);
        joinedHelpText.setVisibility(View.INVISIBLE);

        TextView notJoinedHelpText = (TextView) findViewById(R.id.not_joined_help_text);
        notJoinedHelpText.setVisibility(View.INVISIBLE);

        // Event lists
        RecyclerView eventList = (RecyclerView) findViewById(R.id.event_list);
        LinearLayoutManager eventListLayoutManager = new LinearLayoutManager(this);
        eventList.setLayoutManager(eventListLayoutManager);
        DividerItemDecoration eventListDividerItemDecoration = new DividerItemDecoration(
                eventList.getContext(),
                eventListLayoutManager.getOrientation()
        );
        eventList.addItemDecoration(eventListDividerItemDecoration);


        RecyclerView joinedEventList = (RecyclerView) findViewById(R.id.joined_events_list);
        LinearLayoutManager joinedEventListLayoutManager = new LinearLayoutManager(this);
        joinedEventList.setLayoutManager(joinedEventListLayoutManager);
        DividerItemDecoration joinedEventListDividerItemDecoration = new DividerItemDecoration(
                joinedEventList.getContext(),
                joinedEventListLayoutManager.getOrientation()
        );
        joinedEventList.addItemDecoration(joinedEventListDividerItemDecoration);

        // Set empty adapters
        eventList.setAdapter(new EventListAdapter(Collections.emptyList()));
        joinedEventList.setAdapter(new EventListAdapter(Collections.emptyList()));

        this.model = ViewModelProviders.of(this, factory).get(EventPickingModel.class);
        this.model.init();
        this.model.getEventsPair().observe(this, list -> {
            if (list == null) {
                return;
            }

            eventList.setAdapter(new EventListAdapter(list.getOtherEvents()));
            joinedEventList.setAdapter(new EventListAdapter(list.getJoinedEvents()));

            if (!list.getJoinedEvents().isEmpty()) {
                joinedHelpText.setVisibility(View.VISIBLE);
                notJoinedHelpText.setVisibility(View.VISIBLE);
            } else {
                joinedHelpText.setVisibility(View.INVISIBLE);
                notJoinedHelpText.setVisibility(View.INVISIBLE);
            }
        });

    }
}
