package ch.epfl.sweng.eventmanager.ui.event.interaction.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.widget.*;

import java.util.Collections;

import androidx.annotation.NonNull;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import ch.epfl.sweng.eventmanager.R;
import ch.epfl.sweng.eventmanager.ui.event.interaction.EventShowcaseActivity;
import ch.epfl.sweng.eventmanager.ui.tools.ImageLoader;
import dagger.android.support.AndroidSupportInjection;

import javax.inject.Inject;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Locale;
/**
 * Our main view on the 'visitor' side of the event. Displays a general description of the event.
 */
public class EventMainFragment extends AbstractFeedbackFragment {
    private static final String TAG = "EventMainFragment";

    @Inject
    ImageLoader loader;

    @BindView(R.id.main_fragment_news)
    Button news;
    @BindView(R.id.main_fragment_schedule)
    Button schedule;
    @BindView(R.id.main_fragment_map)
    Button map;
    @BindView(R.id.more_feedback_button)
    Button feedback;
    @BindView(R.id.feedback_ratingBar)
    RatingBar feedbackBar;
    @BindView(R.id.event_description)
    TextView eventDescription;
    @BindView(R.id.event_image)
    ImageView eventImage;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    @BindView(R.id.event_begin_date)
    TextView beginDate;
    @BindView(R.id.event_end_date)
    TextView endDate;
    @BindView(R.id.event_date_linear_layout)
    LinearLayout dateLinearLayout;

    @BindView(R.id.event_feedback_linear_layout)
    LinearLayout eventFeedback;
    @BindView(R.id.event_feedback_recycler_view)
    RecyclerView recyclerView;

    private EventShowcaseActivity showcaseActivity;

    private RatingsRecyclerViewAdapter ratingsRecyclerViewAdapter;

    public EventMainFragment() {
        // Required empty public constructor
        super(R.layout.fragment_event_main);
    }

    public static EventMainFragment newInstance() {
        return new EventMainFragment();
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
            requireActivity().setTitle(ev.getName());

            eventDescription.setText(ev.getDescription());
            eventDescription.setVisibility(View.VISIBLE);

            loader.loadImageWithSpinner(ev, getContext(), eventImage, null);

            eventImage.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);

            feedbackBar.setIsIndicator(true);
            repository.getMeanRating(ev.getId()).observe(this, feedbackBar::setRating);

            if (ev.getBeginDateAsDate() != null) {
                SimpleDateFormat f = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault());
                String beginDateAsString = f.format(ev.getBeginDateAsDate());
                beginDate.setText(String.format("%s %s", getString(R.string.starts_on), beginDateAsString));
                dateLinearLayout.setVisibility(View.VISIBLE);
                if (ev.getEndDateAsDate() != null) {
                    String endDateAsString = f.format(ev.getEndDate());
                    if (!endDateAsString.equals(beginDateAsString)) {
                        endDate.setText(String.format("%s   %s", getString(R.string.Ends_on), endDateAsString));
                        endDate.setVisibility(View.VISIBLE);
                    } else {
                        endDate.setVisibility(View.GONE);
                    }
                }
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        if (view != null) ButterKnife.bind(this, view);

        ratingsRecyclerViewAdapter = new RatingsRecyclerViewAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(ratingsRecyclerViewAdapter);
        //The following is to allow smooth scrolling in the main scrollView.
        recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener()  {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                return true; //Consume the touch event
            }

            @Override
            public void onTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {}

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {}
        });

        model.getEvent().observe(this, ev -> repository.getRatings(ev.getId()).observe(this, ratings -> {
            if (ratings != null && ratings.size() > 0) {
                //display only the most recent feedback
                Collections.sort(ratings, (o1, o2) -> -1 * Long.compare(o1.getDate(), o2.getDate()));
                ratingsRecyclerViewAdapter.setContent(ratings.subList(0, Math.min(3, ratings.size())));
                eventFeedback.setVisibility(View.VISIBLE);
                feedbackBar.setVisibility(View.VISIBLE);
            } else {
                eventFeedback.setVisibility(View.GONE);
                feedbackBar.setVisibility(View.GONE);
            }
        }));

        showcaseActivity = (EventShowcaseActivity) getParentActivity();

        // FIXME Handle NullPointerExceptions from the ChangeFragment
        news.setOnClickListener(v -> showcaseActivity.switchFragment(EventShowcaseActivity.FragmentType.NEWS, true));

        map.setOnClickListener(v -> showcaseActivity.switchFragment(EventShowcaseActivity.FragmentType.MAP, true));

        schedule.setOnClickListener(v -> showcaseActivity.switchFragment(EventShowcaseActivity.FragmentType.SCHEDULE,
                true));

        feedback.setOnClickListener(v -> showcaseActivity.switchFragment(EventShowcaseActivity.FragmentType.EVENT_FEEDBACK, true));

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
        MenuItem menuItem = menu.findItem(R.id.menu_showcase_activity_join_switch);
        menuItem.setVisible(true);
        super.onCreateOptionsMenu(menu, inflater);
    }
}
