package ch.epfl.sweng.eventmanager.ui.schedule;

import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import ch.epfl.sweng.eventmanager.R;
import ch.epfl.sweng.eventmanager.repository.data.Concert;
import ch.epfl.sweng.eventmanager.ui.eventSelector.EventPickingActivity;
import ch.epfl.sweng.eventmanager.viewmodel.ViewModelFactory;
import dagger.android.AndroidInjection;

import javax.inject.Inject;

import java.util.Collections;


public class ScheduleActivity extends AppCompatActivity {

    @Inject
    ViewModelFactory factory;
    private ScheduleViewModel model;

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
        timeLineAdapter = new TimeLineAdapter();
        recyclerView.setAdapter(timeLineAdapter);

        Intent intent = getIntent();
        int eventID = intent.getIntExtra(EventPickingActivity.SELECTED_EVENT_ID, -1);

        if (eventID <= 0) {
            Log.e(TAG, "Got invalid event ID#" + eventID + ".");
        } else {
            this.model = ViewModelProviders.of(this, factory).get(ScheduleViewModel.class);
            this.model.init(eventID);
            this.model.getConcerts().observe(this, concerts -> {
                if (concerts != null) {
                    Collections.sort(concerts, (Concert o1, Concert o2) -> {
                        if (o1.getDate().before(o2.getDate())) {
                            return -1;
                        } else if (o1.getDate().equals(o2.getDate())) {
                            return 0;
                        } else return 1;
                    });
                timeLineAdapter.setDataList(concerts);
                } else {
                    showAlertOnEmptyConcerts(recyclerView);
                }
            });
        }

    }

    /**
     * Show an alert message if there are no registered concerts for this event
     * @param view view
     */
    private void showAlertOnEmptyConcerts(View view) {
        AlertDialog.Builder myAlert = new AlertDialog.Builder(this);
        myAlert.setMessage(R.string.concerts_empty).create();
        myAlert.setOnDismissListener((DialogInterface dialog) -> finish());
        myAlert.show();
    }

}

