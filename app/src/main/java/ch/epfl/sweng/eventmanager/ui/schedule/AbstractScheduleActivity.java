package ch.epfl.sweng.eventmanager.ui.schedule;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import ch.epfl.sweng.eventmanager.R;
import ch.epfl.sweng.eventmanager.repository.JoinedScheduleItemRepository;
import ch.epfl.sweng.eventmanager.repository.data.ScheduledItem;
import ch.epfl.sweng.eventmanager.ui.eventSelector.EventPickingActivity;
import ch.epfl.sweng.eventmanager.viewmodel.ViewModelFactory;
import dagger.android.AndroidInjection;

import javax.inject.Inject;
import java.util.List;

public abstract class AbstractScheduleActivity extends AppCompatActivity {

    @Inject
    ViewModelFactory factory;
    @Inject
    JoinedScheduleItemRepository joinedScheduleItemRepository;
    protected ScheduleViewModel model;

    private static String TAG = "ScheduleActivity";

    private RecyclerView recyclerView;
    private TimeLineAdapter timeLineAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        Intent intent = getIntent();
        int eventID = intent.getIntExtra(EventPickingActivity.SELECTED_EVENT_ID, -1);

        if (eventID <= 0) {
            Log.e(TAG, "Got invalid event ID#" + eventID + ".");
        } else {
            this.model = ViewModelProviders.of(this, factory).get(ScheduleViewModel.class);
            this.model.init(eventID);
            this.getScheduledItems().observe(this, concerts -> {
                timeLineAdapter = new TimeLineAdapter(eventID, concerts, joinedScheduleItemRepository);
                recyclerView.setAdapter(timeLineAdapter);

                // TODO: we should display a message when concerts == null to let the user know that this event has no schedule
            });
        }

    }

    protected abstract LiveData<List<ScheduledItem>> getScheduledItems();


}

