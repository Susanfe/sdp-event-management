package ch.epfl.sweng.eventmanager.ui.event.interaction.fragments.schedule;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import ch.epfl.sweng.eventmanager.R;
import ch.epfl.sweng.eventmanager.repository.data.ScheduledItem;
import ch.epfl.sweng.eventmanager.ui.event.interaction.models.ScheduleViewModel;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public abstract class AbstractScheduleFragment extends Fragment {

    private static String TAG = "AbstractScheduleFragment";
    protected ScheduleViewModel model;
    private TimeLineAdapter timeLineAdapter;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.scheduled_items_empty_tv)
    TextView emptyListTextView;


    protected abstract int getLayout();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(getLayout(), container, false);

        ButterKnife.bind(this,view);
        setEmptyListTextView();
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
            model = ViewModelProviders.of(requireActivity()).get(ScheduleViewModel.class);
        }

        this.getScheduledItems().observe(this, items -> {
            if (items != null && items.size() > 0) {
                emptyListTextView.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                Collections.sort(items,(c1,c2) -> Objects.requireNonNull(c1.getDate()).compareTo(c2.getDate()));
                timeLineAdapter.setDataList(items);
            } else {
                emptyListTextView.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
            }

            onItemsUpdate(items);
        });
    }

    protected void onItemsUpdate(List<ScheduledItem> items) {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        model = ViewModelProviders.of(requireActivity()).get(ScheduleViewModel.class);
    }

    protected abstract void setEmptyListTextView();

    protected abstract LiveData<List<ScheduledItem>> getScheduledItems();


}

