package ch.epfl.sweng.eventmanager.ui.eventShowcase;

import android.content.Context;
import android.net.Uri;
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
import ch.epfl.sweng.eventmanager.repository.data.Event;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link EventMainFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EventMainFragment extends Fragment {
    private static final String TAG = "vlurps";
    private EventShowcaseModel model;

    public EventMainFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment EventMainFragment.
     */
    public static EventMainFragment newInstance(EventShowcaseModel model) {
        EventMainFragment fragment = new EventMainFragment();
        Bundle args = new Bundle();
        args.putSerializable("model", (Serializable) model);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            model = (EventShowcaseModel) getArguments().getSerializable("model");
        }

        // FIXME: handle null model
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
