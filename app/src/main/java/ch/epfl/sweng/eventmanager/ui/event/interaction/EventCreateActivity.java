package ch.epfl.sweng.eventmanager.ui.event.interaction;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ch.epfl.sweng.eventmanager.R;
import ch.epfl.sweng.eventmanager.repository.EventRepository;
import ch.epfl.sweng.eventmanager.repository.data.Event;
import ch.epfl.sweng.eventmanager.ui.event.selection.EventPickingActivity;
import ch.epfl.sweng.eventmanager.ui.tools.ImageLoader;
import ch.epfl.sweng.eventmanager.users.Session;
import ch.epfl.sweng.eventmanager.viewmodel.ViewModelFactory;
import com.google.android.gms.tasks.Task;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.yalantis.ucrop.UCrop;
import dagger.android.AndroidInjection;

import javax.inject.Inject;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class EventCreateActivity extends AppCompatActivity {
    private static final String TAG = "EventCreate";
    private static final int PICK_IMAGE = 1;
    private static final String FILE_EXTENSION_WEBP = "webp";
    private static final int IMAGE_COMPRESS_QUALITY = 70;
    private static final int MAX_IMAGE_WIDTH = 600;
    private static final int MAX_IMAGE_HEIGHT = 300;

    @Inject
    ViewModelFactory factory;

    @Inject
    EventRepository repository;

    @Inject
    Session session;

    @Inject
    ImageLoader imageLoader;

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
    @BindView(R.id.create_form_upload_image)
    Button uploadImageBtn;
    @BindView(R.id.create_form_image_view)
    ImageView eventImage;
    @BindView(R.id.create_form)
    View createForm;
    @BindView(R.id.create_form_switch_visibility)
    SwitchMaterial eventVisibility;
    @BindView(R.id.create_form_delete_event_button)
    Button deleteEventButton;

    private int eventID;
    private Event event;
    private Uri eventImageSrc;
    private Boolean imageChanged = false;
    private boolean loading = true;

    private void populateForm(Event event) {
        this.event = event;
        this.name.setText(event.getName(), TextView.BufferType.EDITABLE);
        this.email.setText(event.getOrganizerEmail(), TextView.BufferType.EDITABLE);
        this.twitter.setText(event.getTwitterName(), TextView.BufferType.EDITABLE);
        this.description.setText(event.getDescription(), TextView.BufferType.EDITABLE);
        this.beginDate.setText(formatDate(event.getBeginDateAsDate()));
        this.endDate.setText(formatDate(event.getBeginDateAsDate()));
        this.eventVisibility.setChecked(event.isVisibleFromPublic());
        imageLoader.loadImageWithSpinner(event, this, this.eventImage, null);
    }

    private void populateEvent() {
        this.event.setName(getFieldValue(this.name));
        this.event.setOrganizerEmail(getFieldValue(this.email));
        this.event.setTwitterName(getFieldValue(this.twitter));
        this.event.setDescription(getFieldValue(this.description));
        this.event.setBeginDate(getDateValue(this.beginDate));
        this.event.setEndDate(getDateValue(this.endDate));
        this.event.setVisibleFromPublic(eventVisibility.isChecked());
    }

    private boolean checkForm() {
        String name = getFieldValue(this.name);

        Log.i("EventCreateActivity", "name='" + name + "'");
        if (name == null) {
            Toast.makeText(this, R.string.create_event_name_empty, Toast.LENGTH_LONG).show();
            return false;
        }

        if (name.length() < 4) {
            Toast.makeText(this, R.string.create_event_name_too_short, Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }

    private String formatDate(Date date) {
        if (date != null) {
            String format = "dd/MM/yyyy";
            SimpleDateFormat dateFormat = new SimpleDateFormat(format, Locale.getDefault());
            return dateFormat.format(date);
        } else {
            return null;
        }
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
            Map<String, String> users = new HashMap<>();
            users.put(session.getCurrentUser().getUid(), "admin");

            event.setUsers(users);

            return repository.createEvent(event);
        } else {
            return repository.updateEvent(event);
        }
    }

    @OnClick(R.id.create_form_upload_image)
    public void setupSelectImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Image"), PICK_IMAGE);
    }

    private void setupButton() {
        this.sendButton.setOnClickListener(v -> {
            if (loading) {
                return; // The button should not be displayed anyway
            }

            if (!checkForm()) {
                return; // Cannot process if the form is not valid
            }

            this.sendButton.setEnabled(false);

            populateEvent(); // Update the event object

            prepareCreationTask().addOnSuccessListener(event -> {
                //upload event image to storage if changed
                if (imageChanged) {
                    this.repository.uploadImage(event, eventImageSrc);
                    imageChanged = false;
                }
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
    protected void onResume() {
        super.onResume();

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_event_create);

        ButterKnife.bind(this);
        this.setupButton();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri eventImageUri = data.getData();
            cropAndConvertImage(eventImageUri);
        }
        if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
            eventImageSrc = UCrop.getOutput(data);
            imageChanged = true;

            imageLoader.displayImage(this, eventImageSrc, eventImage);
        } else if (resultCode == UCrop.RESULT_ERROR) {
            final Throwable cropError = UCrop.getError(data);
            Log.i(TAG, "Unable to crop image");
            cropError.printStackTrace();
        }
    }

    /**
     * Handle the cropping and conversion of the image
     *
     * @param eventImageUri
     */
    private void cropAndConvertImage(Uri eventImageUri) {
        try {
            File eventImageCropped = File.createTempFile("event_cover", FILE_EXTENSION_WEBP);
            eventImageCropped.deleteOnExit();
            Uri eventImageCroppedUri = Uri.fromFile(eventImageCropped);
            UCrop.Options compressOptions = new UCrop.Options();
            compressOptions.setCompressionFormat(Bitmap.CompressFormat.WEBP);
            compressOptions.setCompressionQuality(IMAGE_COMPRESS_QUALITY);
            UCrop.of(eventImageUri, eventImageCroppedUri).withMaxResultSize(MAX_IMAGE_WIDTH, MAX_IMAGE_HEIGHT).withAspectRatio(16, 9).withOptions(compressOptions).start(this);
        } catch (IOException e) {
            e.printStackTrace();
            Log.i(TAG, "Unable to create tempFile for cropping event image");
        }
    }

    @OnClick(R.id.create_form_delete_event_button)
    public void onClickDeleteEventButton() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.create_event_delete_dialog_content).setTitle(R.string.create_event_delete_dialog_title);
        builder.setPositiveButton(R.string.button_yes, (dialog, id) -> {
            this.repository.deleteEvent(event).addOnCompleteListener(m -> {
                Toast.makeText(this, R.string.delete_successfull, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this,EventPickingActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            });
        });
        builder.setNegativeButton(R.string.button_no, (dialog, id) -> {
            dialog.cancel();
        });
        builder.create().show();
    }
}
