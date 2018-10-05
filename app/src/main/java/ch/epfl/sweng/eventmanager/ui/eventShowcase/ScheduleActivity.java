package ch.epfl.sweng.eventmanager.ui.eventShowcase;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;

import com.github.vipulasri.timelineview.TimelineView;

import java.util.ArrayList;
import java.util.List;

import ch.epfl.sweng.eventmanager.R;
import ch.epfl.sweng.eventmanager.ui.eventShowcase.scheduleUtils.TimeLineAdapter;
import ch.epfl.sweng.eventmanager.ui.eventShowcase.scheduleUtils.TimeLineModel;

public class ScheduleActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<TimeLineModel> mDataList = new ArrayList<>();
    private TimeLineAdapter mTimeLineAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setHasFixedSize(true);
        mDataList.add(new TimeLineModel("Item successfully delivered", ""));
        mDataList.add(new TimeLineModel("Courier is out to delivery your order", "2017-02-12 08:00"));
        mDataList.add(new TimeLineModel("Item has reached courier facility at New Delhi", "2017-02-11 21:00"));
        mDataList.add(new TimeLineModel("Item has been given to the courier", "2017-02-11 18:00"));
        mTimeLineAdapter = new TimeLineAdapter(mDataList);
        recyclerView.setAdapter(mTimeLineAdapter);
    }


}

