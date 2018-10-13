package ch.epfl.sweng.eventmanager.ui.eventShowcase.fragments.schedule;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import ch.epfl.sweng.eventmanager.R;
import ch.epfl.sweng.eventmanager.repository.data.ScheduledItem;
import ch.epfl.sweng.eventmanager.ui.eventShowcase.models.ScheduleViewModel;

import java.util.Collections;
import java.util.List;

public abstract class AbstractScheduleFragment extends Fragment {

    private static String TAG = "AbstractScheduleFragment";
    protected ScheduleViewModel model;
    private RecyclerView recyclerView;
    private TimeLineAdapter timeLineAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_schedule, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recyclerView.setHasFixedSize(true);


        timeLineAdapter = new TimeLineAdapter(model);
        recyclerView.setAdapter(timeLineAdapter);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        Log.i(TAG, "Resume " + model);

        if (model == null) {
            model = ViewModelProviders.of(getActivity()).get(ScheduleViewModel.class);
        }

        this.getScheduledItems().observe(this, concerts -> {
            if (concerts != null) {
                Collections.sort(concerts, (ScheduledItem o1, ScheduledItem o2) -> {
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

    /**
      * Show an alert message if there are no registered concerts for this event
      * @param view view
      */
    private void showAlertOnEmptyConcerts(View view) {
         AlertDialog.Builder myAlert = new AlertDialog.Builder(getActivity());
         myAlert.setMessage(R.string.concerts_empty).create();
         myAlert.show();
     }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        model = ViewModelProviders.of(getActivity()).get(ScheduleViewModel.class);
    }

    protected abstract LiveData<List<ScheduledItem>> getScheduledItems();


}

