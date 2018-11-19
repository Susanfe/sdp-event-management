package ch.epfl.sweng.eventmanager.ui.event.selection;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import ch.epfl.sweng.eventmanager.R;
import ch.epfl.sweng.eventmanager.repository.data.Event;
import ch.epfl.sweng.eventmanager.ui.event.interaction.EventShowcaseActivity;

import java.util.List;

public class EventListAdapter extends RecyclerView.Adapter<EventListAdapter.ViewHolder> {
    private List<Event> mEvents;

    // Provide a reference to the views for each data item
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // each data item is just a string in this case
        @BindView(R.id.event_name)
        TextView eventNameTextView;
        @BindView(R.id.event_summary)
        TextView eventSummary;
        @BindView(R.id.event_thumbnail)
        ImageView eventThumbnail;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
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

    EventListAdapter(List<Event> myEvents) {
        mEvents = myEvents;
    }

    // Create new views (invoked by the layout manager)
    @Override
    @NonNull
    public EventListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.event_list_item, parent, false);
        return new ViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.eventNameTextView.setText(mEvents.get(position).getName());
        holder.eventSummary.setText(mEvents.get(position).getDescription());
        holder.eventThumbnail.setImageBitmap(mEvents.get(position).getImage());
    }

    @Override
    public int getItemCount() {
        return mEvents.size();
    }
}
