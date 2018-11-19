package ch.epfl.sweng.eventmanager.ui.event.interaction.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import butterknife.ButterKnife;
import ch.epfl.sweng.eventmanager.R;

public class EventFeedbackFragment extends AbstractShowcaseFragment {


    public EventFeedbackFragment() {
        super(R.layout.fragment_event_feedback);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_event_form, container, false);
        ButterKnife.bind(this, view);
        return view;
    }



}
