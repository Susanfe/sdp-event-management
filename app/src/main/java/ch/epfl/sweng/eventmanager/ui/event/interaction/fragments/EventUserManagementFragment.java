package ch.epfl.sweng.eventmanager.ui.event.interaction.fragments;

import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import ch.epfl.sweng.eventmanager.R;
import ch.epfl.sweng.eventmanager.ui.event.interaction.fragments.AbstractShowcaseFragment;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 */
public class EventUserManagementFragment extends AbstractShowcaseFragment {
    private static final String TAG = "EventUserManagementFragment";

    public EventUserManagementFragment() {
        // Required empty public constructor
        super(R.layout.fragment_event_user_management);
    }

    @Override
    public void onResume() {
        super.onResume();

        model.getEvent().observe(this, ev -> {
            if (ev == null) {
                Log.e(TAG, "Got null model from parent activity");
                return;
            }
        });
    }
}
