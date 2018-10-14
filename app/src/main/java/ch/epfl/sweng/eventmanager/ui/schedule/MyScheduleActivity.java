package ch.epfl.sweng.eventmanager.ui.schedule;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import ch.epfl.sweng.eventmanager.R;
import ch.epfl.sweng.eventmanager.repository.JoinedScheduleItemRepository;
import ch.epfl.sweng.eventmanager.repository.data.JoinedScheduleItem;
import ch.epfl.sweng.eventmanager.repository.data.ScheduledItem;
import ch.epfl.sweng.eventmanager.ui.eventSelector.EventPickingActivity;
import ch.epfl.sweng.eventmanager.viewmodel.ViewModelFactory;
import dagger.android.AndroidInjection;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class MyScheduleActivity extends AppCompatActivity {
    @Inject
    ViewModelFactory factory;
    @Inject
    JoinedScheduleItemRepository joinedScheduleItemRepository;
    private ScheduleViewModel model;

    private static String TAG = "MyScheduleActivity";

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

        this.model = ViewModelProviders.of(this, factory).get(ScheduleViewModel.class);
        this.model.init(eventID);
        LiveData<List<JoinedScheduleItem>> joinedScheduleActivites = joinedScheduleItemRepository.findByEventId(eventID);

        LiveData<List<ScheduledItem>> scheduleItems = this.model.getConcerts();

        LiveData<List<ScheduledItem>> joinedScheduleItems = Transformations.switchMap(scheduleItems, items -> {
            return Transformations.map(joinedScheduleActivites, joinedScheduleActivity -> {
                List<ScheduledItem> joined = new ArrayList<>();

                    for (ScheduledItem scheduledItem : items) {
                        if (joinedScheduleActivity.contains(new JoinedScheduleItem(scheduledItem.getId(), eventID)))
                            joined.add(scheduledItem);
                    }
                return joined;
            });
        });

        joinedScheduleItems.observe(this, concerts -> {
            timeLineAdapter = new TimeLineAdapter(eventID, concerts, joinedScheduleItemRepository);
            recyclerView.setAdapter(timeLineAdapter);
        });
    }


}
