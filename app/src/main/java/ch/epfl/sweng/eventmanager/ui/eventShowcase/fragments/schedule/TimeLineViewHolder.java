package ch.epfl.sweng.eventmanager.ui.eventShowcase.fragments.schedule;

import android.arch.lifecycle.LiveData;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import android.widget.Toast;
import com.github.vipulasri.timelineview.TimelineView;

import butterknife.BindView;
import butterknife.ButterKnife;
import ch.epfl.sweng.eventmanager.R;

class TimeLineViewHolder extends RecyclerView.ViewHolder {

    private LiveData<Boolean> isEventJoined;
    private Runnable onScheduleAdd;
    private Runnable onScheduleRemove;

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


    TimeLineViewHolder(View itemView, int viewType) {
        super(itemView);

        ButterKnife.bind(this, itemView);
        mTimelineView.initLine(viewType);

        itemView.setOnLongClickListener(v -> {

            Boolean state = isEventJoined.getValue(); // at the time of the call

            if (state == null || !state) {
                TimeLineViewHolder.this.onScheduleAdd.run(); // Join the event
                Toast.makeText(itemView.getContext(), R.string.timeline_view_added_to_own_schedule, Toast.LENGTH_SHORT).show();
            } else {
                TimeLineViewHolder.this.onScheduleRemove.run(); // Leave the event
                Toast.makeText(itemView.getContext(), R.string.timeline_view_removed_from_own_schedule, Toast.LENGTH_SHORT).show();
            }
            return true;
        });
    }


    public TimeLineViewHolder setOnScheduleAdd(Runnable onScheduleAdd) {
        this.onScheduleAdd = onScheduleAdd;
        return this;
    }

    public TimeLineViewHolder setOnScheduleRemove(Runnable onScheduleRemove) {
        this.onScheduleRemove = onScheduleRemove;
        return this;
    }

    public TimeLineViewHolder setIsEventJoined(LiveData<Boolean> isEventJoined) {
        this.isEventJoined = isEventJoined;
        return this;
    }

}