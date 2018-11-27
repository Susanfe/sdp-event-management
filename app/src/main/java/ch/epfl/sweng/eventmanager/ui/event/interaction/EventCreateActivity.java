package ch.epfl.sweng.eventmanager.ui.event.interaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProviders;
import butterknife.BindView;
import butterknife.ButterKnife;
import ch.epfl.sweng.eventmanager.R;
import ch.epfl.sweng.eventmanager.repository.EventRepository;
import ch.epfl.sweng.eventmanager.repository.data.Event;
import ch.epfl.sweng.eventmanager.repository.impl.FirebaseCloudFunction;
import ch.epfl.sweng.eventmanager.ui.event.interaction.fragments.schedule.ScheduleParentFragment;
import ch.epfl.sweng.eventmanager.ui.event.interaction.fragments.user.EventUserManagementFragment;
import ch.epfl.sweng.eventmanager.ui.event.interaction.models.EventInteractionModel;
import ch.epfl.sweng.eventmanager.ui.event.selection.EventPickingActivity;
import ch.epfl.sweng.eventmanager.users.Session;
import ch.epfl.sweng.eventmanager.viewmodel.ViewModelFactory;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import dagger.android.AndroidInjection;

public class EventCreateActivity extends AppCompatActivity {
    private static final String TAG = "EventCreate";

    @Inject
    ViewModelFactory factory;

    @Inject
    EventRepository repository;

    @BindView(R.id.create_form_send_button)
    Button sendButton;
    @BindView(R.id.create_form_name)
    EditText name;
    @BindView(R.id.create_form_email)
    EditText email;
    @BindView(R.id.create_form_twitter_handle)
    EditText twitter;
    @BindView(R.id.create_form_description)
    EditText description;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.create_form)
    View createForm;

    private int eventID;
    private Event event;
    private boolean loading = true;

    private void populateForm(Event event) {
        this.event = event;
        this.name.setText(event.getName(), TextView.BufferType.EDITABLE);
        this.email.setText(event.getOrganizerEmail(), TextView.BufferType.EDITABLE);
        this.twitter.setText(event.getTwitterName(), TextView.BufferType.EDITABLE);
        this.description.setText(event.getDescription(), TextView.BufferType.EDITABLE);
    }

    private void populateEvent() {
        this.event.setName(this.name.getText().toString());
        this.event.setOrganizerEmail(this.email.getText().toString());
        this.event.setTwitterName(this.twitter.getText().toString());
        this.event.setDescription(this.description.getText().toString());
    }

    private void finishLoading() {
        this.progressBar.setVisibility(View.GONE);
        this.createForm.setVisibility(View.VISIBLE);
        this.loading = false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_event_create);
        ButterKnife.bind(this);

        // Setup button
        this.sendButton.setOnClickListener(v -> {
            if (loading) {
                return; // The button should not be displayed anyway
            }

            this.sendButton.setEnabled(false);

            populateEvent(); // Update the event object

            Task<Event> task;
            if (eventID <= 0) {
                // Create the event, then set the user admin of his event
                task = repository.createEvent(event)
                        .continueWithTask(ev -> FirebaseCloudFunction
                                .addUserToEvent(Session.getCurrentUser().getEmail(), ev.getResult().getId(), "admin")
                                .continueWith(b -> ev.getResult())
                );
            } else {
                task = repository.updateEvent(event);
            }

            task.addOnSuccessListener(event -> {
                // Start event administration activity
                if (eventID <= 0) {
                    Toast.makeText(this, R.string.create_event_success, Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(this, R.string.create_event_update_success, Toast.LENGTH_LONG).show();
                }

                Intent adminIntent = new Intent(this, EventAdministrationActivity.class);
                adminIntent.putExtra(EventPickingActivity.SELECTED_EVENT_ID, event.getId());
                startActivity(adminIntent);
            }).addOnFailureListener(failure -> {
                Log.e(TAG, "Event creation or update failure for " + eventID + ": ", failure);
                this.sendButton.setEnabled(true);
                Toast.makeText(this, getText(R.string.create_event_error) + failure.getMessage(), Toast.LENGTH_LONG).show();
            }).addOnCanceledListener(() -> {
                Toast.makeText(this, R.string.create_event_canceled, Toast.LENGTH_LONG).show();
                this.sendButton.setEnabled(true);
            });
        });

        // Fetch event from passed ID
        Intent intent = getIntent();
        eventID = intent.getIntExtra(EventPickingActivity.SELECTED_EVENT_ID, -1);
        if (eventID <= 0) {
            // No event ID: we want to create an event
            setTitle(R.string.create_event_title);
            this.event = new Event();
            finishLoading();
        } else {
            LiveData<Event> event = repository.getEvent(eventID);

            event.observe(this, ev -> {
                if (ev == null) {
                    Log.e(TAG, "Got null model from parent activity");
                    return;
                }

                // Set window title and content
                setTitle("Edit: " + ev.getName());
                populateForm(ev);
                finishLoading();

                // Remove observer, because we don't want the form values to be overriden if the
                // event changes remotely
                // FIXME: or maybe we want that actually?
                event.removeObservers(this);
            });
        }
    }
}
