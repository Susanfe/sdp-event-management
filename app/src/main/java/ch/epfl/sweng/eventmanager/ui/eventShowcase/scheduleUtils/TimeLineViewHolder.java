package ch.epfl.sweng.eventmanager.ui.eventShowcase.scheduleUtils;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.github.vipulasri.timelineview.TimelineView;

import org.w3c.dom.Text;

import butterknife.BindView;
import butterknife.ButterKnife;
import ch.epfl.sweng.eventmanager.R;

class TimeLineViewHolder extends RecyclerView.ViewHolder {

    private TimelineView timelineView;

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
        timelineView.initLine(viewType);
    }
}