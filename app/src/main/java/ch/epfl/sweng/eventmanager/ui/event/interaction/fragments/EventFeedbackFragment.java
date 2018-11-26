package ch.epfl.sweng.eventmanager.ui.event.interaction.fragments;

import android.content.Context;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;
import androidx.annotation.NonNull;
import butterknife.BindView;
import butterknife.ButterKnife;
import ch.epfl.sweng.eventmanager.EventManagerApplication;
import ch.epfl.sweng.eventmanager.R;
import ch.epfl.sweng.eventmanager.repository.FeedbackRepository;
import ch.epfl.sweng.eventmanager.repository.data.EventRating;
import ch.epfl.sweng.eventmanager.ui.event.interaction.EventShowcaseActivity;
import dagger.android.support.AndroidSupportInjection;

import javax.inject.Inject;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

public class EventFeedbackFragment extends AbstractShowcaseFragment {
    private static final String TAG = "EventFeedbackFragment";
    private String UNIQUE_ID_DEVICE;

    @Inject
    protected FeedbackRepository repository;

    @BindView(R.id.feedback_form_send_button)
    Button sendButton;
    @BindView(R.id.feedback_form_content)
    EditText description;
    @BindView(R.id.feedback_form_ratingBar)
    RatingBar rating;

    public EventFeedbackFragment() {
        super(R.layout.fragment_event_feedback);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, view);

        model.getEvent().observe(this, ev -> {
            AtomicReference<Boolean> ratingExists = new AtomicReference<>();
            repository.ratingFromDeviceExists(ev.getId(), UNIQUE_ID_DEVICE).observe(this, ratingExists::set);
            sendButton.setOnClickListener(l -> {
                EventRating newEventRating = new EventRating(UNIQUE_ID_DEVICE, rating.getRating(), description.getText().toString());
                //TODO Use a unique identifier to restrict each device to one feedback
                if (ratingExists.get() == null) ;
                else if (!ratingExists.get()) {
                    //Publish the rating and shows that feedback has been published
                    repository.publishRating(ev.getId(), newEventRating).addOnSuccessListener(aVoid -> {
                        Toast.makeText(getActivity(), R.string.event_feedback_submitted, Toast.LENGTH_SHORT).show();
                        // Returns to main showcase event screen
                        ((EventShowcaseActivity) getActivity()).changeFragment(new EventMainFragment(), true);
                    });
                } else {
                    Toast.makeText(getActivity(), R.string.event_feedback_already_submitted, Toast.LENGTH_SHORT).show();
                }
            });
        });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        AndroidSupportInjection.inject(this);
        UNIQUE_ID_DEVICE = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        super.onAttach(context);
    }
}
