package ch.epfl.sweng.eventmanager.ui.event.interaction.fragments;

import android.content.Context;
import android.os.Bundle;
import android.provider.Settings;
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
import ch.epfl.sweng.eventmanager.R;
import ch.epfl.sweng.eventmanager.repository.FeedbackRepository;
import ch.epfl.sweng.eventmanager.repository.data.Event;
import ch.epfl.sweng.eventmanager.repository.data.EventRating;
import ch.epfl.sweng.eventmanager.ui.event.interaction.EventShowcaseActivity;
import dagger.android.support.AndroidSupportInjection;

import javax.inject.Inject;
import java.util.concurrent.atomic.AtomicReference;

public class SubmitFeedbackFragment extends AbstractShowcaseFragment {
    private static final String TAG = "SubmitFeedbackFragment";
    private String UNIQUE_DEVICE_ID;

    @Inject
    protected FeedbackRepository repository;

    @BindView(R.id.feedback_form_send_button)
    Button sendButton;
    @BindView(R.id.feedback_form_content)
    EditText description;
    @BindView(R.id.feedback_form_ratingBar)
    RatingBar rating;

    public SubmitFeedbackFragment() {
        super(R.layout.fragment_submit_feedback);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        model.getEvent().observe(this, ev -> {
            AtomicReference<Boolean> ratingExists = new AtomicReference<>();
            repository.ratingFromDeviceExists(ev.getId(), UNIQUE_DEVICE_ID).observe(this, ratingExists::set);
            sendButton.setOnClickListener(l -> {
                submitEventRating(ratingExists.get(), ev);
            });
        });
    }

    @Override
    public void onAttach(Context context) {
        AndroidSupportInjection.inject(this);
        UNIQUE_DEVICE_ID = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        super.onAttach(context);
    }

    public void submitEventRating(Boolean ratingExists, Event ev){
        EventRating newEventRating = new EventRating(UNIQUE_DEVICE_ID, rating.getRating(), description.getText().toString(), System.currentTimeMillis());
        if (!ratingExists) {
            //Publish the rating and shows that feedback has been published
            repository.publishRating(ev.getId(), newEventRating).addOnSuccessListener(aVoid -> {
                Toast.makeText(getActivity(), R.string.event_feedback_submitted, Toast.LENGTH_SHORT).show();
                // Returns to main showcase event screen
                ((EventShowcaseActivity) getActivity()).switchFragment(EventShowcaseActivity.FragmentType.MAIN,true);
            });
        } else {
            Toast.makeText(getActivity(), R.string.event_feedback_already_submitted, Toast.LENGTH_SHORT).show();
        }
    }
}
