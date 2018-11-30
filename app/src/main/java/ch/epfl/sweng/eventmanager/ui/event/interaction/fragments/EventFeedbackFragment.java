package ch.epfl.sweng.eventmanager.ui.event.interaction.fragments;

import android.content.Context;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import ch.epfl.sweng.eventmanager.R;
import ch.epfl.sweng.eventmanager.repository.FeedbackRepository;
import ch.epfl.sweng.eventmanager.repository.data.EventRating;
import ch.epfl.sweng.eventmanager.ui.event.interaction.EventShowcaseActivity;
import dagger.android.support.AndroidSupportInjection;

import javax.inject.Inject;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class EventFeedbackFragment extends AbstractShowcaseFragment {
    private static final String TAG = "EventFeedbackFragment";
    private String UNIQUE_ID_DEVICE;

    @Inject
    protected FeedbackRepository repository;

    @BindView(R.id.feedback_submit_feedback)
    Button submitFeedback;
    @BindView(R.id.feedback_recycler_view)
    RecyclerView recyclerView;

    private RatingsAdapter ratingsAdapter = new RatingsAdapter();

    public EventFeedbackFragment() {
        super(R.layout.fragment_display_feedback);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, view);

        recyclerView.setAdapter(ratingsAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        model.getEvent().observe(this, ev -> {

            AtomicReference<Boolean> ratingExists = new AtomicReference<>();
            repository.ratingFromDeviceExists(ev.getId(), UNIQUE_ID_DEVICE).observe(this, ratingExists::set);

            if (ratingExists.get() != null && ratingExists.get()){
                submitFeedback.setVisibility(View.INVISIBLE);
            }

            repository.getRatings(ev.getId()).observe(this, ratings -> {
                if (ratings != null && ratings.size() > 0) {
                    recyclerView.setVisibility(View.VISIBLE);
                    ratingsAdapter.setContent(ratings);
                } else {
                    recyclerView.setVisibility(View.GONE);
                    ratingsAdapter.setContent(Collections.emptyList());
                }
            });
        });

        submitFeedback.setOnClickListener(l -> ((EventShowcaseActivity) getActivity()).changeFragment(new SubmitFeedbackFragment(), true));
    }

    @Override
    public void onAttach(Context context) {
        AndroidSupportInjection.inject(this);
        UNIQUE_ID_DEVICE = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        super.onAttach(context);
    }

    public class RatingsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private List<EventRating> ratingList = Collections.emptyList();

        @NonNull
        @Override
        public RatingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            // create a new view
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_feedback, parent, false);

            return new RatingViewHolder(v);
        }

        public void setContent(List<EventRating> ratings) {
            this.ratingList = ratings;
            this.notifyDataSetChanged();
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            if (holder instanceof RatingsAdapter.RatingViewHolder)
                ((RatingsAdapter.RatingViewHolder) holder).bind(ratingList.get(position));
        }

        @Override
        public int getItemCount() {
            return ratingList.size();
        }

        final class RatingViewHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.item_feedback_rating)
            RatingBar rating;
            @BindView(R.id.item_feedback_description)
            TextView comment;
            @BindView(R.id.item_feedback_date)
            TextView date;

            RatingViewHolder(View v) {
                super(v);
                ButterKnife.bind(this, v);
            }

            public void bind(EventRating rating) {
                this.rating.setRating(rating.getRating());
                this.comment.setText(rating.getDescription());
                this.date.setText(rating.getDate().toString());
            }
        }
    }

}
