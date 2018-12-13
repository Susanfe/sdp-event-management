package ch.epfl.sweng.eventmanager.ui.event.interaction.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import butterknife.BindView;
import butterknife.ButterKnife;
import ch.epfl.sweng.eventmanager.R;
import ch.epfl.sweng.eventmanager.notifications.JoinedEventFeedbackStrategy;
import ch.epfl.sweng.eventmanager.notifications.JoinedEventStrategy;
import ch.epfl.sweng.eventmanager.notifications.NotificationScheduler;
import ch.epfl.sweng.eventmanager.repository.FeedbackRepository;
import ch.epfl.sweng.eventmanager.ui.event.interaction.EventShowcaseActivity;
import dagger.android.support.AndroidSupportInjection;

import javax.inject.Inject;

/**
 * Our main view on the 'visitor' side of the event. Displays a general description of the event.
 */
public class EventMainFragment extends AbstractShowcaseFragment {
    private static final String TAG = "EventMainFragment";

    @Inject
    protected FeedbackRepository feedbackRepository;

    @BindView(R.id.contact_form_go_button)
    Button contactButton;
    @BindView(R.id.main_fragment_news)
    Button news;
    @BindView(R.id.main_fragment_schedule)
    Button schedule;
    @BindView(R.id.main_fragment_map)
    Button map;
    @BindView(R.id.feedback_for_go_button)
    Button feedbackButton;
    @BindView(R.id.feedback_ratingBar)
    RatingBar feedbackBar;
    @BindView(R.id.join_event_button)
    CheckedTextView joinEventButton;
    @BindView(R.id.event_description)
    TextView eventDescription;
    @BindView(R.id.event_image)
    ImageView eventImage;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    private EventShowcaseActivity showcaseActivity;

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
            // Set window title
            getActivity().setTitle(ev.getName());

            eventDescription.setText(ev.getDescription());
            eventDescription.setVisibility(View.VISIBLE);

            ev.loadEventImageIntoImageView(getContext(),eventImage);
            eventImage.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);

            feedbackBar.setIsIndicator(true);
            feedbackRepository.getMeanRating(ev.getId()).observe(this, feedbackBar::setRating);

            // Binds the 'joined event' switch to the database
            CheckedTextView joinEventButton = view.findViewById(R.id.join_event_button);

            // State of the switch depends on if the user joined the event
            this.model.isJoined(ev).observe(this, joinEventButton::setChecked);
            joinEventButton.setOnClickListener(v -> {
                if (!joinEventButton.isChecked()) {
                    this.model.joinEvent(ev);
                    NotificationScheduler.scheduleNotification(ev, new JoinedEventStrategy(getContext()));
                    NotificationScheduler.scheduleNotification(ev, new JoinedEventFeedbackStrategy(getContext()));
                } else {
                    this.model.unjoinEvent(ev);
                    NotificationScheduler.unscheduleNotification(ev, new JoinedEventStrategy(getContext()));
                    NotificationScheduler.unscheduleNotification(ev, new JoinedEventFeedbackStrategy(getContext()));
                }
            });
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        if (view != null) ButterKnife.bind(this, view);

        showcaseActivity = (EventShowcaseActivity) getParentActivity();

        // FIXME Handle NullPointerExceptions from the ChangeFragment
        contactButton.setOnClickListener(v -> showcaseActivity.callChangeFragment(
                EventShowcaseActivity.FragmentType.FORM, true));

        news.setOnClickListener(v -> showcaseActivity.callChangeFragment(
                EventShowcaseActivity.FragmentType.NEWS, true));

        map.setOnClickListener(v -> showcaseActivity.callChangeFragment(
                EventShowcaseActivity.FragmentType.MAP, true));

        schedule.setOnClickListener(v -> showcaseActivity.callChangeFragment(
                EventShowcaseActivity.FragmentType.SCHEDULE, true));

        feedbackButton.setOnClickListener(v -> ((EventShowcaseActivity) getActivity()).changeFragment(new EventFeedbackFragment(), true));

        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onAttach(Context context) {
        AndroidSupportInjection.inject(this);
        super.onAttach(context);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        MenuItem menuItem = menu.findItem(R.id.menu_showcase_activity_join_id);
        menuItem.setVisible(true);
        super.onCreateOptionsMenu(menu, inflater);
    }
}
