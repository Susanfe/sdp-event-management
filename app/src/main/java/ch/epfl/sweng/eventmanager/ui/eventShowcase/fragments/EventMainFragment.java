package ch.epfl.sweng.eventmanager.ui.eventShowcase.fragments;

import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import ch.epfl.sweng.eventmanager.R;
import ch.epfl.sweng.eventmanager.notifications.JoinedEventStrategy;
import ch.epfl.sweng.eventmanager.notifications.NotificationScheduler;
import ch.epfl.sweng.eventmanager.ui.eventShowcase.EventShowcaseActivity;

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
                Log.e(TAG, "Got null event");
                return;
            }

            // Set window title
            getActivity().setTitle(ev.getName());

            TextView eventDescription = view.findViewById(R.id.event_description);
            eventDescription.setText(ev.getDescription());
            Button contactButton = view.findViewById(R.id.contact_form_go_button);
            contactButton.setOnClickListener(v ->
                    ((EventShowcaseActivity)getActivity()).changeFragment(
                            new EventFormFragment(), true));

            ImageView eventLogo = view.findViewById(R.id.event_image);
            model.getEventImage().observe(this, eventLogo::setImageBitmap);

            // Binds the 'joined event' switch to the database
            Switch joinEventSwitch = view.findViewById(R.id.join_event_switch);
            // State of the switch depends on if the user joined the event
            this.model.isJoined(ev).observe(this, joinEventSwitch::setChecked);
            joinEventSwitch.setOnClickListener(v -> {
                if (joinEventSwitch.isChecked()) {
                    this.model.joinEvent(ev);
                    NotificationScheduler.scheduleNotification(ev, new JoinedEventStrategy(getContext()));
                }
                else {
                    this.model.unjoinEvent(ev);
                    NotificationScheduler.unscheduleNotification(ev, new JoinedEventStrategy(getContext()));
                }
            });
        });
    }
}
