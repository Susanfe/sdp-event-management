package ch.epfl.sweng.eventmanager.ui.schedule;

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
import ch.epfl.sweng.eventmanager.repository.data.Concert;
import com.github.vipulasri.timelineview.TimelineView;

import java.util.List;
import java.util.Locale;

import ch.epfl.sweng.eventmanager.R;


public class TimeLineAdapter extends RecyclerView.Adapter<TimeLineViewHolder> {

    private List<Concert> dataList;
    private Context context;
    private LayoutInflater mLayoutInflater;


    /**
     * Gets the TimeLine view type for the item at a given position (start, middle, end).
     * @param position position of the iterated item in the list held of the array adapter.
     * @return the view type (int) of the TimeLine view to use for this element.
     */
    @Override
    public int getItemViewType(int position) {
        if (position < 0) position = -2; // Triggers the Line.NORMAL view id to be returned.
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

        Concert concert = dataList.get(position);

        String artistText = concert.getArtist() == null ? context.getString(R.string.concert_no_artist) : concert.getArtist();
        String dateText = concert.dateAsString() == null ? context.getString(R.string.concert_no_date) : concert.dateAsString();
        String genreText = concert.getGenre() == null ? context.getString(R.string.concert_no_genre) : concert.getGenre();
        String descriptionText = concert.getDescription() == null ? context.getString(R.string.concert_no_genre) : concert.getDescription();
        String durationText = transformDuration(concert.getDuration());

        holder.mArtist.setText(artistText);
        holder.mDate.setText(dateText);
        holder.mGenre.setText(genreText);
        holder.mDescription.setText(descriptionText);
        holder.mDuration.setText(durationText);

        Concert.ConcertStatus status = concert.getStatus();

        // Sets Marker according to the concert status
        if (status == Concert.ConcertStatus.PASSED) {
            holder.mTimelineView.setMarker(getMarkerDrawable(context, R.drawable.ic_marker_inactive, android.R.color.darker_gray));
        } else if (status == Concert.ConcertStatus.IN_PROGRESS) {
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

     public void setDataList(List<Concert> dataList) {
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

    TimeLineViewHolder(View itemView, int viewType) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        mTimelineView.initLine(viewType);
    }
}
