package ch.epfl.sweng.eventmanager.ui.event.interaction.fragments;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import ch.epfl.sweng.eventmanager.R;
import ch.epfl.sweng.eventmanager.repository.FeedbackRepository;
import ch.epfl.sweng.eventmanager.repository.data.EventRating;

import javax.inject.Inject;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public abstract class AbstractFeedbackFragment extends AbstractShowcaseFragment {

    @Inject
    protected FeedbackRepository repository;

    AbstractFeedbackFragment(int resource) {
        super(resource);
    }

    public class RatingsRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
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

            Collections.sort(ratingList, (o1, o2) -> -1 * Long.compare(o1.getDate(), o2.getDate()));

            this.notifyDataSetChanged();
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            if (holder instanceof RatingsRecyclerViewAdapter.RatingViewHolder)
                ((RatingsRecyclerViewAdapter.RatingViewHolder) holder).bind(ratingList.get(position));
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
                this.date.setText(new Date(rating.getDate()).toString());
            }
        }
    }

}
