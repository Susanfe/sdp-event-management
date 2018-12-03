package ch.epfl.sweng.eventmanager.ui.event.interaction.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import ch.epfl.sweng.eventmanager.R;

/**
 * Allows an administrator a manage the users allowed to work with this event (= access to admin
 * features).
 */
public class EventTicketManagementFragment extends AbstractShowcaseFragment {
    private static final String TAG = "EventTicketManagement";

    public EventTicketManagementFragment() {
        // Required empty public constructor
        super(R.layout.fragment_event_ticket_management);
    }

    @Override
    public void onResume() {
        super.onResume();

        model.getEvent().observe(this, ev -> {
            if (ev == null) {
                Log.e(TAG, "Got null model from parent activity");
            }

        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, view);

        return view;
    }
}
