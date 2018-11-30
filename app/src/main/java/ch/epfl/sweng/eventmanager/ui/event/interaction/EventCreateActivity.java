package ch.epfl.sweng.eventmanager.ui.event.interaction;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ch.epfl.sweng.eventmanager.R;
import ch.epfl.sweng.eventmanager.repository.EventRepository;
import ch.epfl.sweng.eventmanager.repository.data.Event;
import ch.epfl.sweng.eventmanager.ui.event.selection.EventPickingActivity;
import ch.epfl.sweng.eventmanager.users.Session;
import ch.epfl.sweng.eventmanager.viewmodel.ViewModelFactory;
import com.google.android.gms.tasks.Task;
import dagger.android.AndroidInjection;

import javax.inject.Inject;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

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
    @BindView(R.id.create_form_begin_date)
    EditText beginDate;
    @BindView(R.id.create_form_end_date)
    EditText endDate;
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
        this.beginDate.setText(formatDate(event.getBeginDate()));
        this.endDate.setText(formatDate(event.getEndDate()));
    }

    private void populateEvent() {
        this.event.setName(getFieldValue(this.name));
        this.event.setOrganizerEmail(getFieldValue(this.email));
        this.event.setTwitterName(getFieldValue(this.twitter));
        this.event.setDescription(getFieldValue(this.description));
        this.event.setBeginDate(getDateValue(this.beginDate));
        this.event.setEndDate(getDateValue(this.endDate));
    }

    private String formatDate(Date date) {
        String format = "dd/MM/yyyy";
        SimpleDateFormat dateFormat = new SimpleDateFormat(format, Locale.getDefault());
        return dateFormat.format(date);
    }

    private long getDateValue(EditText editText) {
        String format = "dd/MM/yyyy";
        long date = 0L;
        SimpleDateFormat dateFormat = new SimpleDateFormat(format, Locale.getDefault());
        try {
            date = dateFormat.parse(editText.getText().toString()).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
            Log.i(TAG, "unable to parse date");
        }
        return date;
    }

    private String getFieldValue(EditText field) {
        String s = field.getText().toString();

        return s.isEmpty() ? null : s;
    }

    private void finishLoading() {
        this.progressBar.setVisibility(View.GONE);
        this.createForm.setVisibility(View.VISIBLE);
        this.loading = false;
    }

    private Task<Event> prepareCreationTask() {
        if (eventID <= 0) {
            // Create the event and set the user admin of his event
            Map<String, Map<String, String>> users = new HashMap<>();
            users.put("admin", new HashMap<>());
            users.get("admin").put("originalOwner", Session.getCurrentUser().getUid());

            event.setUsers(users);

            return repository.createEvent(event);
        } else {
            return repository.updateEvent(event);
        }
    }

    @OnClick(R.id.create_form_send_button)
    public void onClickSendButton() {
        setupButton();
    }

    private void setupButton() {
        this.sendButton.setOnClickListener(v -> {
            if (loading) {
                return; // The button should not be displayed anyway
            }

            this.sendButton.setEnabled(false);

            populateEvent(); // Update the event object

            prepareCreationTask().addOnSuccessListener(event -> {
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
    }

    @OnClick(R.id.create_form_begin_date)
    void onClickBeginDate() {
        pickDateDialog(beginDate, System.currentTimeMillis() - 1000);
    }

    @OnClick(R.id.create_form_end_date)
    void onClickEndDate() {
        pickDateDialog(endDate, getDateValue(beginDate));
    }

    private void pickDateDialog(EditText dateField, long minDate) {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener date = (view, year, month, dayOfMonth) -> {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            dateField.setText(formatDate(calendar.getTime()));
        };
        DatePickerDialog datePickerDialog = new DatePickerDialog(EventCreateActivity.this, date,
                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.getDatePicker().setMinDate(minDate);
        datePickerDialog.show();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_event_create);

        ButterKnife.bind(this);

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
