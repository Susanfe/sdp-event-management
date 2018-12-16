package ch.epfl.sweng.eventmanager.ui.event.interaction.fragments.schedule;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import androidx.annotation.NonNull;
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import ch.epfl.sweng.eventmanager.notifications.JoinedScheduledItemStrategy;
import ch.epfl.sweng.eventmanager.notifications.NotificationScheduler;
import ch.epfl.sweng.eventmanager.repository.data.ScheduledItem;
import ch.epfl.sweng.eventmanager.ui.event.interaction.models.ScheduleViewModel;
import com.github.vipulasri.timelineview.TimelineView;

import java.util.List;
import java.util.Locale;

import ch.epfl.sweng.eventmanager.R;


public class TimeLineAdapter extends RecyclerView.Adapter<TimeLineAdapter.TimeLineViewHolder> {

    /*
     * Triggers the Line.NORMAL view id to be returned by `TimelineView.getTimeLineViewType/2`.
     * @see <a href="https://github.com/vipulasri/Timeline-View/blob/v1.0.6/timelineview/src/main/java/com/github/vipulasri/timelineview/TimelineView.java#L269">TimelineView source code</a>
     */
    private final int TIMELINEVIEW_LINE_NORMAL_POSITION = -2;

    private List<ScheduledItem> dataList;
    private Context context;
    private LayoutInflater mLayoutInflater;
    private AbstractScheduleFragment fragment;

    TimeLineAdapter(AbstractScheduleFragment fragment) {
        this.fragment = fragment;
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
        holder.setItem(scheduledItem);

        ScheduledItem.ScheduledItemStatus status = scheduledItem.getStatus();

        // Sets Marker according to the scheduledItem status
        if (status == ScheduledItem.ScheduledItemStatus.NOT_STARTED) {
            holder.mTimelineView.setMarker(getMarkerDrawable(context, R.drawable.ic_marker_inactive, android.R.color.darker_gray));
        } else if (status == ScheduledItem.ScheduledItemStatus.IN_PROGRESS) {
            holder.mTimelineView.setMarker(getMarkerDrawable(context, R.drawable.ic_marker_active, R.color.colorPrimary));
        } else {
            holder.mTimelineView.setMarker(ContextCompat.getDrawable(context, R.drawable.ic_marker), ContextCompat.getColor(context, R.color.colorPrimary));
        }
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
        @BindView(R.id.text_item_room)
        TextView mItemRoom;
        @BindView(R.id.time_marker)
        TimelineView mTimelineView;
        private ScheduledItem item;


        TimeLineViewHolder(View itemView, int viewType) {
            super(itemView);

            ButterKnife.bind(this, itemView);
            mTimelineView.initLine(viewType);

            itemView.setOnLongClickListener(v -> {
                fragment.onLongPress(item);
                return true;
            });

            itemView.setOnClickListener(v -> {
                fragment.onShortPress(item);
            });
        }

        void setItem(ScheduledItem item) {
            this.item = item;

            String artistText = item.getArtist() == null ? context.getString(R.string.scheduled_item_no_artist) : item.getArtist();
            String dateText = item.dateAsString() == null ? context.getString(R.string.scheduled_item_no_date) : item.dateAsString();
            String genreText = item.getGenre() == null ? context.getString(R.string.scheduled_item_no_genre) : item.getGenre();
            String descriptionText = item.getDescription() == null ? context.getString(R.string.scheduled_item_no_description) : item.getDescription();
            String durationText = transformDuration(item.getDuration());
            String room = item.getItemLocation() == null ? "" : item.getItemLocation();

            mArtist.setText(artistText);
            mDate.setText(dateText);
            mGenre.setText(genreText);
            mDescription.setText(descriptionText);
            mDuration.setText(durationText);
            mItemRoom.setText(room);
        }
    }
}
