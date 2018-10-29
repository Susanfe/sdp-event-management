package ch.epfl.sweng.eventmanager.ui.eventShowcase.fragments.schedule;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import ch.epfl.sweng.eventmanager.notifications.ScheduledItemNotification;
import ch.epfl.sweng.eventmanager.repository.data.JoinedScheduleItem;
import ch.epfl.sweng.eventmanager.repository.data.ScheduledItem;
import ch.epfl.sweng.eventmanager.ui.eventShowcase.models.ScheduleViewModel;
import com.github.vipulasri.timelineview.TimelineView;

import java.util.List;
import java.util.Locale;

import ch.epfl.sweng.eventmanager.R;


public class TimeLineAdapter extends RecyclerView.Adapter<TimeLineViewHolder> {

    /*
     * Triggers the Line.NORMAL view id to be returned by `TimelineView.getTimeLineViewType/2`.
     * @see <a href="https://github.com/vipulasri/Timeline-View/blob/v1.0.6/timelineview/src/main/java/com/github/vipulasri/timelineview/TimelineView.java#L269">TimelineView source code</a>
     */
    private final int TIMELINEVIEW_LINE_NORMAL_POSITION = -2;

    private List<ScheduledItem> dataList;
    private Context context;
    private LayoutInflater mLayoutInflater;
    private ScheduleViewModel model;

    TimeLineAdapter(ScheduleViewModel model) {
        this.model = model;
    }

    /**
     * Gets the TimeLine view type for the item at a given position (start, middle, end).
     * @param position position of the iterated item in the list held of the array adapter.
     * @return the view type (int) of the TimeLine view to use for this element.
     */
    @Override
    public int getItemViewType(int position) {
        if (position < 0) position = TIMELINEVIEW_LINE_NORMAL_POSITION;
        return TimelineView.getTimeLineViewType(position,getItemCount());
    }

    @NonNull
    @Override
    public TimeLineViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        mLayoutInflater = LayoutInflater.from(context);
        View view = mLayoutInflater.inflate(R.layout.item_timeline, parent, false);
        return new TimeLineViewHolder(view, viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull TimeLineViewHolder holder, int position) {

        ScheduledItem scheduledItem = dataList.get(position);

        String artistText = scheduledItem.getArtist() == null ? context.getString(R.string.scheduled_item_no_artist) : scheduledItem.getArtist();
        String dateText = scheduledItem.dateAsString() == null ? context.getString(R.string.scheduled_item_no_date) : scheduledItem.dateAsString();
        String genreText = scheduledItem.getGenre() == null ? context.getString(R.string.scheduled_item_no_genre) : scheduledItem.getGenre();
        String descriptionText = scheduledItem.getDescription() == null ? context.getString(R.string.scheduled_item_no_description) : scheduledItem.getDescription();
        String durationText = transformDuration(scheduledItem.getDuration());

        holder.mArtist.setText(artistText);
        holder.mDate.setText(dateText);
        holder.mGenre.setText(genreText);
        holder.mDescription.setText(descriptionText);
        holder.mDuration.setText(durationText);

        ScheduledItem.ScheduledItemStatus status = scheduledItem.getStatus();

        // Sets Marker according to the scheduledItem status
        if (status == ScheduledItem.ScheduledItemStatus.NOT_STARTED) {
            holder.mTimelineView.setMarker(getMarkerDrawable(context, R.drawable.ic_marker_inactive, android.R.color.darker_gray));
        } else if (status == ScheduledItem.ScheduledItemStatus.IN_PROGRESS) {
            holder.mTimelineView.setMarker(getMarkerDrawable(context, R.drawable.ic_marker_active, R.color.colorPrimary));
        } else {
            holder.mTimelineView.setMarker(ContextCompat.getDrawable(context, R.drawable.ic_marker), ContextCompat.getColor(context, R.color.colorPrimary));
        }

        holder.setOnToggle(() -> {
            model.toggleMySchedule(scheduledItem.getId(), wasAdded -> {

                if (wasAdded) {
                    Toast.makeText(context, R.string.timeline_view_added_to_own_schedule, Toast.LENGTH_SHORT).show();
                    ScheduledItemNotification.scheduleNotification(context, scheduledItem);
                } else {
                    Toast.makeText(context, R.string.timeline_view_removed_from_own_schedule, Toast.LENGTH_SHORT).show();
                    ScheduledItemNotification.unscheduleNotification(context, scheduledItem);
                }
            });
        });
    }

    @Override
    public int getItemCount() {
        return (dataList != null ? dataList.size() : 0);
    }

    private String transformDuration(double duration) {
        double intPart = Math.floor(duration);
        int fractionalPart = (int)((duration - intPart)*60);
        return (int)intPart+"h"+ String.format(Locale.getDefault(),"%02d", fractionalPart);
    }


    private static Drawable getMarkerDrawable(Context context, int drawableResId, int colorFilter) {
        Drawable drawable = VectorDrawableCompat.create(context.getResources(), drawableResId, context.getTheme());
            if (drawable == null) {
                Log.i("ERROR", "setColorFilter method returned null in TimeLineAdapter while " +
                        "fetching marker drawable");
                return null;
            }
            drawable.setColorFilter(ContextCompat.getColor(context, colorFilter), PorterDuff.Mode.SRC_IN);
            return drawable;
    }

    void setDataList(List<ScheduledItem> dataList) {
        this.dataList = dataList;
        this.notifyDataSetChanged();
    }
}

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

    void setOnToggle(Runnable onToggle) {
        this.onToggle = onToggle;
    }
}