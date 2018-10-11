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

import ch.epfl.sweng.eventmanager.repository.data.Concert;
import com.github.vipulasri.timelineview.TimelineView;

import java.util.List;

import ch.epfl.sweng.eventmanager.R;


public class TimeLineAdapter extends RecyclerView.Adapter<TimeLineViewHolder> {

    private List<Concert> dataList;
    private Context context;
    private LayoutInflater mLayoutInflater;

    public TimeLineAdapter(List<Concert> feedList) {
        dataList = feedList;
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
        return (dataList !=null? dataList.size():0);
    }

    private String transformDuration(double duration) {
        double intPart = Math.floor(duration);
        int fractionalPart = (int)((duration - intPart)*60);
        return (int)intPart+"h"+String.format("%02d", fractionalPart);
    }

    private static Drawable getMarkerDrawable(Context context, int drawableResId, int colorFilter) {
        Drawable drawable = VectorDrawableCompat.create(context.getResources(), drawableResId, context.getTheme());
        try {
            drawable.setColorFilter(ContextCompat.getColor(context, colorFilter), PorterDuff.Mode.SRC_IN);
        } catch (NullPointerException n) {
            Log.i("ERROR", "setColorFilter method returned null in TimeLineAdapter while " +
                    "fetching marker drawable");
        }
        return drawable;
    }
}
