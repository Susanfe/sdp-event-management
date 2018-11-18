package ch.epfl.sweng.eventmanager.ui.eventSelector;

import android.content.Context;
import android.content.Intent;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ch.epfl.sweng.eventmanager.R;
import ch.epfl.sweng.eventmanager.repository.data.Event;
import ch.epfl.sweng.eventmanager.ui.eventShowcase.EventShowcaseActivity;

import java.util.List;

public class EventListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Event> mEvents;

    public enum ItemType {JoinedEvents, Event}

    ;
    private ItemType itemType;

    public EventListAdapter(List<Event> myEvents, ItemType itemType) {
        mEvents = myEvents;
        this.itemType = itemType;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        switch (itemType) {
            case Event:
                View eventView = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_list_item,
                        parent, false);
                vh = new EventViewHolder(eventView);
                break;
            case JoinedEvents:
                View joinedEventView =
                        LayoutInflater.from(parent.getContext()).inflate(R.layout.joined_event_list_item,
                                parent, false);
                vh = new JoinedEventViewHolder(joinedEventView);
                break;
            default:
                vh = null;
                break;
        }
        return vh;
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (itemType) {
            case Event:
                eventOnBindViewHolder((EventViewHolder) holder, position);
                break;

            case JoinedEvents:
                joinedEventOnBindViewHolder((JoinedEventViewHolder) holder, position);
                break;
        }
    }

    private void eventOnBindViewHolder(EventViewHolder holder, int position) {
        holder.eventNameTextView.setText(mEvents.get(position).getName());
        holder.eventSummary.setText(mEvents.get(position).getDescription());
        holder.eventThumbnail.setImageBitmap(mEvents.get(position).getImage());
    }

    private void joinedEventOnBindViewHolder(JoinedEventViewHolder holder, int position) {
        holder.eventNameTextView.setText(mEvents.get(position).getName());
        holder.eventSummary.setText(mEvents.get(position).getDescription());
        holder.eventThumbnail.setImageBitmap(mEvents.get(position).getImage());
    }


    @Override
    public int getItemCount() {
        return mEvents.size();
    }


    // Provide a reference to the views for each data item
    public class JoinedEventViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // each data item is just a string in this case
        @BindView(R.id.event_name)
        TextView eventNameTextView;
        @BindView(R.id.event_summary)
        TextView eventSummary;
        @BindView(R.id.event_thumbnail)
        ImageView eventThumbnail;

        public JoinedEventViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            // TODO: display animation on click?
            Context context = itemView.getContext();
            Intent intent = new Intent(context, EventShowcaseActivity.class);
            Event selectedEvent = mEvents.get(getAdapterPosition());
            intent.putExtra(EventPickingActivity.SELECTED_EVENT_ID, selectedEvent.getId());
            context.startActivity(intent);
        }
    }

    public class EventViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.event_name)
        TextView eventNameTextView;
        @BindView(R.id.event_summary)
        TextView eventSummary;
        @BindView(R.id.event_thumbnail)
        ImageView eventThumbnail;
        @BindView(R.id.goto_event_btn)
        Button goToEvent;
        @BindView(R.id.join_event_btn)
        Button joinEvent;

        public EventViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
