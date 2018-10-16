package ch.epfl.sweng.eventmanager.ui.eventShowcase.fragments.schedule;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import ch.epfl.sweng.eventmanager.R;
import com.github.vipulasri.timelineview.TimelineView;

import java.util.function.Consumer;
import java.util.function.Supplier;


class TimeLineViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.text_timeline_date)
    TextView mDate;
    @BindView(R.id.text_timeline_artist)
    TextView mArtist;
    @BindView(R.id.text_timeline_genre)
    TextView mGenre;
    @BindView(R.id.text_timeline_description)
    TextView mDescription;
    @BindView(R.id.text_timeline_duration)
    TextView mDuration;
    @BindView(R.id.time_marker)
    TimelineView mTimelineView;
    private Runnable onToggle;


    TimeLineViewHolder(View itemView, int viewType) {
        super(itemView);

        ButterKnife.bind(this, itemView);
        mTimelineView.initLine(viewType);

        itemView.setOnLongClickListener(v -> {
            onToggle.run();
            return true;
        });
    }

    public void setOnToggle(Runnable onToggle) {
        this.onToggle = onToggle;
    }
}