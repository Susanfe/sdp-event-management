package ch.epfl.sweng.eventmanager.ui.event.selection;

import android.content.Context;
import android.content.Intent;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
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
import ch.epfl.sweng.eventmanager.ui.event.interaction.EventShowcaseActivity;

import java.util.ArrayList;
import java.util.List;

public class EventListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Event> mEvents;

    void update(List<Event> events) {
        class EventDiffCallback extends DiffUtil.Callback {
            private final List<Event> oldEventList;
            private final List<Event> newEventList;

            private EventDiffCallback(List<Event> oldEventList, List<Event> newEventList) {
                this.oldEventList = oldEventList;
                this.newEventList = newEventList;
            }
            @Override
            public int getOldListSize() {
                return oldEventList.size();
            }

            @Override
            public int getNewListSize() {
                return newEventList.size();
            }

            @Override
            public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                return oldEventList.get(oldItemPosition).getId() == newEventList.get(newItemPosition).getId();
            }

            @Override
            public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                return oldEventList.get(oldItemPosition).equals(newEventList.get(newItemPosition));
            }
        }
        final EventDiffCallback diffCallback = new EventDiffCallback(this.mEvents,events);
        final DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffCallback);
        diffResult.dispatchUpdatesTo(this);
        this.mEvents.clear();
        this.mEvents.addAll(events);
    }

    public enum ItemType {JoinedEvents, Event}
    private ItemType itemType;

    private EventListAdapter(List<Event> myEvents, ItemType itemType) {
        mEvents = myEvents;
        this.itemType = itemType;
    }

    static EventListAdapter newInstance(ItemType itemType) {
        List<Event> emptyList = new ArrayList<>();
        return new EventListAdapter(emptyList,itemType);
    }

    // Create new views (invoked by the layout manager)
    @Override
    @NonNull
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
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

        JoinedEventViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            goToEvent(itemView,getAdapterPosition());
        }

    }

    class EventViewHolder extends RecyclerView.ViewHolder {

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

        EventViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick(R.id.goto_event_btn)
        void onClickEventButton() {
            goToEvent(itemView,getAdapterPosition());
        }

        @OnClick(R.id.join_event_btn)
        void onClickJoinButton() {
            Context context = itemView.getContext();
            ((EventPickingActivity) context).joinEvent(mEvents.get(getAdapterPosition()));
        }
    }

    private void goToEvent(View itemView, int eventPosition) {
        // TODO: display animation on click?
        Context context = itemView.getContext();
        Intent intent = new Intent(context, EventShowcaseActivity.class);
        Event selectedEvent = mEvents.get(eventPosition);
        intent.putExtra(EventPickingActivity.SELECTED_EVENT_ID, selectedEvent.getId());
        context.startActivity(intent);
    }
}
