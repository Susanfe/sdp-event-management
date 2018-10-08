package ch.epfl.sweng.eventmanager.ui.eventShowcase.scheduleUtils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
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

    @Override
    public TimeLineViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        mLayoutInflater = LayoutInflater.from(mContext);
        View view = mLayoutInflater.inflate(R.layout.event_list_item, parent, false);
        return new TimeLineViewHolder(view, viewType);
    }

    @Override
    public void onBindViewHolder(TimeLineViewHolder holder, int position) {

        TimeLineModel timeLineModel = dataList.get(position);

        String artistText = timeLineModel.getArtist().isEmpty() ? Concert.NO_ARTIST : timeLineModel.getArtist();
        String dateText = timeLineModel.getDate().isEmpty() ? NO_DATE : timeLineModel.getDate();
        String genreText = timeLineModel.getGenre().isEmpty() ? Concert.NO_GENRE : timeLineModel.getGenre();
        String descriptionText = timeLineModel.getDescription().isEmpty() ? Concert.NO_DESCRIPTION : timeLineModel.getDescription();

        holder.mArtist.setText(artistText);
        holder.mDate.setText(dateText);
        holder.mGenre.setText(genreText);
        holder.mDescription.setText(descriptionText);

        // Need to implement the TimeLine Marker's state

    }

    @Override
    public int getItemCount() {
        return (dataList !=null? dataList.size():0);
    }
}
