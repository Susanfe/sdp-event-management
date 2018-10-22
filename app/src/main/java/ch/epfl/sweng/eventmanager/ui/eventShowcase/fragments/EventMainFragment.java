package ch.epfl.sweng.eventmanager.ui.eventShowcase.fragments;

import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.Switch;
import android.widget.TextView;
import ch.epfl.sweng.eventmanager.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 */
public class EventMainFragment extends AbstractShowcaseFragment {
    private static final String TAG = "EventMainFragment";

    public EventMainFragment() {
        // Required empty public constructor
        super(R.layout.fragment_event_main);
    }

    @Override
    public void onResume() {
        super.onResume();

        model.getEvent().observe(this, ev -> {
            if (ev == null) {
                Log.e(TAG, "Got null model from parent activity");
                return;
            }

            // Set window title
            getActivity().setTitle(ev.getName());

            TextView eventDescription = (TextView) view.findViewById(R.id.event_description);
            eventDescription.setText(ev.getDescription());

            // Binds the 'joined event' switch to the database
            Switch joinEventSwitch = (Switch) view.findViewById(R.id.join_event_switch);
            // State of the switch depends on if the user joined the event
            this.model.isJoined(ev).observe(this, joinEventSwitch::setChecked);
            joinEventSwitch.setOnClickListener(v -> {
                if (joinEventSwitch.isChecked()) this.model.joinEvent(ev);
                else this.model.unjoinEvent(ev);
            });
        });
    }
}
