package ch.epfl.sweng.eventmanager.ui.eventSelector;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import ch.epfl.sweng.eventmanager.R;
import ch.epfl.sweng.eventmanager.data.Event;
import ch.epfl.sweng.eventmanager.ui.eventShowcase.EventShowcaseActivity;

import java.util.List;

public class EventListAdapter extends RecyclerView.Adapter<EventListAdapter.ViewHolder> {
    private List<Event> mEvents;

    // Provide a reference to the views for each data item
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // each data item is just a string in this case
        public TextView eventNameTextView;
        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            eventNameTextView = (TextView) itemView.findViewById(R.id.event_name);
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

    public EventListAdapter(List<Event> myEvents) {
        mEvents = myEvents;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public EventListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        View v = (View) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.event_list_item, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.eventNameTextView.setText(mEvents.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return mEvents.size();
    }
}
