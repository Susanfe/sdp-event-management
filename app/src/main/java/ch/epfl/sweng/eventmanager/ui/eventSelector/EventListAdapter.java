package ch.epfl.sweng.eventmanager.ui.eventSelector;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import ch.epfl.sweng.eventmanager.R;
import ch.epfl.sweng.eventmanager.repository.data.Event;

import java.util.List;

public class EventListAdapter extends RecyclerView.Adapter<EventListAdapter.ViewHolder> {
    private List<Event> mEvents;

    // Provide a reference to the views for each data item
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView eventNameTextView;
        public Button selectEventButton;
        public ViewHolder(View itemView) {
            super(itemView);
            eventNameTextView = (TextView) itemView.findViewById(R.id.event_name);
            selectEventButton = (Button) itemView.findViewById(R.id.select_event_button);
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
        holder.selectEventButton.setText("Select »");
    }

    @Override
    public int getItemCount() {
        return mEvents.size();
    }
}
