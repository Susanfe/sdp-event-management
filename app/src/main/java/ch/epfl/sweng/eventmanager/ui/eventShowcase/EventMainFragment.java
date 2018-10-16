package ch.epfl.sweng.eventmanager.ui.eventShowcase;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;

import java.io.Serializable;

import ch.epfl.sweng.eventmanager.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 */
public class EventMainFragment extends Fragment {
    private static final String TAG = "EventMainFragment";
    private EventShowcaseModel model;

    public EventMainFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        model = ViewModelProviders.of(getActivity()).get(EventShowcaseModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_event_main, container, false);
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

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
