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
import androidx.lifecycle.LiveData;
import butterknife.BindView;
import butterknife.ButterKnife;
import ch.epfl.sweng.eventmanager.R;
import ch.epfl.sweng.eventmanager.repository.FeedbackRepository;
import ch.epfl.sweng.eventmanager.repository.data.EventRating;
import ch.epfl.sweng.eventmanager.ui.event.interaction.EventShowcaseActivity;
import com.google.android.gms.tasks.OnSuccessListener;
import dagger.android.support.AndroidSupportInjection;

import javax.inject.Inject;
import java.util.UUID;

public class EventFeedbackFragment extends AbstractShowcaseFragment {
    private static final String UNIQUE_ID_DEVICE = UUID.randomUUID().toString();

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
    //TODO check for empty rating field
        model.getEvent().observe(this, ev -> {
            sendButton.setOnClickListener(l -> {
                EventRating newEventRating = new EventRating(UNIQUE_ID_DEVICE, rating.getRating(), description.getText().toString());
                //TODO Use the unique identifier to restrict each device to one feedback
              //  LiveData<Boolean> eventRatingLiveData = repository.isRatingAlreadyPublished(ev.getId(), UNIQUE_ID_DEVICE);
              //  eventRatingLiveData.observe(this, eventRating -> {
                 //   if (!eventRating) {
                        //Publish the rating and shows that feedback has been published
                        repository.publishRating(ev.getId(), newEventRating).addOnSuccessListener(aVoid -> {
                            //        Toast.makeText(getActivity(), "Your feedback has been submitted !", Toast.LENGTH_SHORT).show();
                            // Returns to main showcase event screen
                            ((EventShowcaseActivity) getActivity()).changeFragment(new EventMainFragment(), true);
                        });
                //    } else {
                    //    Toast.makeText(getActivity(), "Your feedback has already been submitted !", Toast.LENGTH_SHORT).show();
                //    }
             //   });
            });
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        AndroidSupportInjection.inject(this);
        super.onAttach(context);
    }
}
