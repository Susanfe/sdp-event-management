package ch.epfl.sweng.eventmanager.ui.event.interaction.fragments;

import android.content.Context;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import ch.epfl.sweng.eventmanager.R;
import ch.epfl.sweng.eventmanager.ui.event.interaction.EventShowcaseActivity;
import dagger.android.support.AndroidSupportInjection;

import java.util.Collections;
import java.util.concurrent.atomic.AtomicReference;

public class EventFeedbackFragment extends AbstractFeedbackFragment {
    private static final String TAG = "EventFeedbackFragment";
    private String UNIQUE_DEVICE_ID;

    @BindView(R.id.feedback_submit_feedback)
    Button submitFeedback;
    @BindView(R.id.ratings_empty_tv)
    TextView emptyRatingsTextView;
    @BindView(R.id.feedback_recycler_view)
    RecyclerView recyclerView;

    private RatingsRecyclerViewAdapter ratingsRecyclerViewAdapter = new RatingsRecyclerViewAdapter();

    public EventFeedbackFragment() {
        super(R.layout.fragment_display_feedback);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, view);

        recyclerView.setAdapter(ratingsRecyclerViewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        model.getEvent().observe(this, ev -> {

            AtomicReference<Boolean> ratingExists = new AtomicReference<>();
            repository.ratingFromDeviceExists(ev.getId(), UNIQUE_DEVICE_ID).observe(this, ratingExists::set);

            repository.getRatings(ev.getId()).observe(this, ratings -> {
                if (ratings != null && ratings.size() > 0) {
                    recyclerView.setVisibility(View.VISIBLE);
                    emptyRatingsTextView.setVisibility(View.GONE);
                    ratingsRecyclerViewAdapter.setContent(ratings);
                } else {
                    recyclerView.setVisibility(View.GONE);
                    submitFeedback.setVisibility(View.VISIBLE);
                    ratingsRecyclerViewAdapter.setContent(Collections.emptyList());
                }
            });

            submitFeedback.setOnClickListener(l -> {
                if (ratingExists.get() != null && !ratingExists.get())
                    ((EventShowcaseActivity) getActivity()).changeFragment(new SubmitFeedbackFragment(), true);
                else Toast.makeText(getActivity(), R.string.event_feedback_already_submitted, Toast.LENGTH_SHORT).show();
            });
        });
    }

    @Override
    public void onAttach(Context context) {
        AndroidSupportInjection.inject(this);
        UNIQUE_DEVICE_ID = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        super.onAttach(context);
    }


}
