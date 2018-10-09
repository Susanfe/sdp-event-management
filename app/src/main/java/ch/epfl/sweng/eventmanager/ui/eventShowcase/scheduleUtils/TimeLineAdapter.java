package ch.epfl.sweng.eventmanager.ui.eventShowcase.scheduleUtils;

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

import com.github.vipulasri.timelineview.TimelineView;

import java.util.List;

import ch.epfl.sweng.eventmanager.R;
import ch.epfl.sweng.eventmanager.repository.data.Concert;


public class TimeLineAdapter extends RecyclerView.Adapter<TimeLineViewHolder> {

    private List<TimeLineModel> dataList;
    private Context mContext;
    private LayoutInflater mLayoutInflater;

    private static final String NO_DATE = "2018-1-1 20:30";

    public TimeLineAdapter(List<TimeLineModel> feedList) {
        dataList = feedList;
    }

    @Override
    public int getItemViewType(int position) {
        return TimelineView.getTimeLineViewType(position,getItemCount());
    }

    @NonNull
    @Override
    public TimeLineViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        mLayoutInflater = LayoutInflater.from(mContext);
        View view = mLayoutInflater.inflate(R.layout.event_list_item, parent, false);
        return new TimeLineViewHolder(view, viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull TimeLineViewHolder holder, int position) {

        TimeLineModel timeLineModel = dataList.get(position);

        String artistText = timeLineModel.getArtist().isEmpty() ? Concert.NO_ARTIST : timeLineModel.getArtist();
        String dateText = timeLineModel.getStringDate().isEmpty() ? NO_DATE : timeLineModel.getStringDate();
        String genreText = timeLineModel.getGenre().isEmpty() ? Concert.NO_GENRE : timeLineModel.getGenre();
        String descriptionText = timeLineModel.getDescription().isEmpty() ? Concert.NO_DESCRIPTION : timeLineModel.getDescription();
        String durationText = transformDuration(timeLineModel.getDuration());

        holder.mArtist.setText(artistText);
        holder.mDate.setText(dateText);
        holder.mGenre.setText(genreText);
        holder.mDescription.setText(descriptionText);
        holder.mDuration.setText(durationText);

        // Sets Marker according to the concert status
        if (timeLineModel.getIsNow() == -1) {
            holder.mTimelineView.setMarker(getMarkerDrawable(mContext, R.drawable.ic_marker_inactive, android.R.color.darker_gray));
        } else if (timeLineModel.getIsNow() == 0) {
            holder.mTimelineView.setMarker(getMarkerDrawable(mContext, R.drawable.ic_marker_active, R.color.colorPrimary));
        } else {
            holder.mTimelineView.setMarker(ContextCompat.getDrawable(mContext, R.drawable.ic_marker), ContextCompat.getColor(mContext, R.color.colorPrimary));
        }
    }

    @Override
    public int getItemCount() {
        return (dataList !=null? dataList.size():0);
    }

    private String transformDuration(double duration) {
        double intPart = Math.abs(duration);
        double fractionalPart = Math.round((duration - intPart)*60 / 10d)*10d;
        return intPart+":"+fractionalPart;
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
