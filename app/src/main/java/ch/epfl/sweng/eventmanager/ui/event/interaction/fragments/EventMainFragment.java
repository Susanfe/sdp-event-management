package ch.epfl.sweng.eventmanager.ui.event.interaction.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import ch.epfl.sweng.eventmanager.R;
import ch.epfl.sweng.eventmanager.notifications.JoinedEventStrategy;
import ch.epfl.sweng.eventmanager.notifications.NotificationScheduler;
import ch.epfl.sweng.eventmanager.ui.event.interaction.EventShowcaseActivity;
import ch.epfl.sweng.eventmanager.ui.event.interaction.fragments.schedule.ScheduleParentFragment;

/**
 * Our main view on the 'visitor' side of the event. Displays a general description of the event.
 */
public class EventMainFragment extends AbstractShowcaseFragment {
    private static final String TAG = "EventMainFragment";

    @BindView(R.id.contact_form_go_button)
    Button contactButton;
    @BindView(R.id.main_fragment_news)
    Button news;
    @BindView(R.id.main_fragment_schedule)
    Button schedule;
    @BindView(R.id.main_fragment_map)
    Button map;

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

            // FIXME handle NullPointerException in setTitle
            // Set window titlze
            getActivity().setTitle(ev.getName());

            TextView eventDescription = view.findViewById(R.id.event_description);
            eventDescription.setText(ev.getDescription());

            ImageView eventLogo = view.findViewById(R.id.event_image);
            eventLogo.setImageBitmap(ev.getImage());

            // Binds the 'joined event' switch to the database
            CheckedTextView joinEventButton = view.findViewById(R.id.join_event_button);
            // State of the switch depends on if the user joined the event
            this.model.isJoined(ev).observe(this, joinEventButton::setChecked);
            joinEventButton.setOnClickListener(v -> {
                if (!joinEventButton.isChecked()) {
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        // FIXME Handle null veiw argument
        ButterKnife.bind(this, view);

        // FIXME Handle NullPointerExceptions from the ChangeFragment
        contactButton.setOnClickListener(v ->
                ((EventShowcaseActivity)getActivity()).changeFragment(
                        new EventFormFragment(), true));

        news.setOnClickListener(v -> ((EventShowcaseActivity)getActivity()).changeFragment(
                new NewsFragment(), true));

        map.setOnClickListener(v -> ((EventShowcaseActivity)getActivity()).changeFragment(
                new EventMapFragment(), true));

        schedule.setOnClickListener(v -> ((EventShowcaseActivity)getActivity()).changeFragment(
                new ScheduleParentFragment(), true));

        return view;

    }
}
