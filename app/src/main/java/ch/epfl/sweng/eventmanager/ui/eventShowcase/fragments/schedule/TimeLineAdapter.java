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

import butterknife.BindView;
import butterknife.ButterKnife;
import ch.epfl.sweng.eventmanager.repository.data.ScheduledItem;
import ch.epfl.sweng.eventmanager.ui.eventShowcase.models.ScheduleViewModel;
import com.github.vipulasri.timelineview.TimelineView;

import java.util.List;
import java.util.Locale;

import ch.epfl.sweng.eventmanager.R;


public class TimeLineAdapter extends RecyclerView.Adapter<TimeLineViewHolder> {

    private List<ScheduledItem> dataList;
    private Context context;
    private LayoutInflater mLayoutInflater;
    private ScheduleViewModel model;

    TimeLineAdapter(List<ScheduledItem> feedList, ScheduleViewModel model) {
        dataList = feedList;
        this.model = model;
    }

    @Override
    public int getItemViewType(int position) {
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

        String artistText = scheduledItem.getArtist() == null ? context.getString(R.string.concert_no_artist) : scheduledItem.getArtist();
        String dateText = scheduledItem.dateAsString() == null ? context.getString(R.string.concert_no_date) : scheduledItem.dateAsString();
        String genreText = scheduledItem.getGenre() == null ? context.getString(R.string.concert_no_genre) : scheduledItem.getGenre();
        String descriptionText = scheduledItem.getDescription() == null ? context.getString(R.string.concert_no_genre) : scheduledItem.getDescription();
        String durationText = transformDuration(scheduledItem.getDuration());

        holder.mArtist.setText(artistText);
        holder.mDate.setText(dateText);
        holder.mGenre.setText(genreText);
        holder.mDescription.setText(descriptionText);
        holder.mDuration.setText(durationText);

        ScheduledItem.ScheduledItemStatus status = scheduledItem.getStatus();

        // Sets Marker according to the scheduledItem status
        if (status == ScheduledItem.ScheduledItemStatus.PASSED) {
            holder.mTimelineView.setMarker(getMarkerDrawable(context, R.drawable.ic_marker_inactive, android.R.color.darker_gray));
        } else if (status == ScheduledItem.ScheduledItemStatus.IN_PROGRESS) {
            holder.mTimelineView.setMarker(getMarkerDrawable(context, R.drawable.ic_marker_active, R.color.colorPrimary));
        } else {
            holder.mTimelineView.setMarker(ContextCompat.getDrawable(context, R.drawable.ic_marker), ContextCompat.getColor(context, R.color.colorPrimary));
        }

        holder.setOnToggle(() -> model.toggleMySchedule(scheduledItem.getId(), context));
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

    public void setOnToggle(Runnable onToggle) {
        this.onToggle = onToggle;
    }
}