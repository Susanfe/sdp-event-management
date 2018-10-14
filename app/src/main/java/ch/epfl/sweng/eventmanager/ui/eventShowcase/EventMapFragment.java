package ch.epfl.sweng.eventmanager.ui.eventShowcase;

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
 * Use the {@link EventMapFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EventMapFragment extends Fragment {
    private static final String TAG = "EventMapFragment";
    private EventShowcaseModel model;

    public EventMapFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment EventMainFragment.
     */
    public static EventMapFragment newInstance(EventShowcaseModel model) {
        EventMapFragment fragment = new EventMapFragment();
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

        View view = inflater.inflate(R.layout.fragment_event_map, container, false);
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
