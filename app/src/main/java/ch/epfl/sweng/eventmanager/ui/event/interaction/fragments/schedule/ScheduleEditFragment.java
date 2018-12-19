package ch.epfl.sweng.eventmanager.ui.event.interaction.fragments.schedule;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ch.epfl.sweng.eventmanager.R;
import ch.epfl.sweng.eventmanager.repository.data.ScheduledItem;
import ch.epfl.sweng.eventmanager.ui.event.interaction.MultiFragmentActivity;
import ch.epfl.sweng.eventmanager.ui.event.interaction.models.ScheduleViewModel;
import ch.epfl.sweng.eventmanager.utils.DateUtils;

import java.util.Calendar;

public class ScheduleEditFragment extends Fragment {
    private static final String TAG = "ScheduleEditFragment";
    private static final String SCHEDULE_ITEM = "schedule_item";

    @BindView(R.id.create_form_send_button)
    Button sendButton;
    @BindView(R.id.create_form_begin_date)
    EditText beginDate;
    @BindView(R.id.create_form_begin_time)
    EditText beginTime;
    @BindView(R.id.create_form_artist)
    EditText artist;
    @BindView(R.id.create_form_genre)
    EditText genre;
    @BindView(R.id.create_form_room)
    EditText room;
    @BindView(R.id.create_form_duration)
    EditText duration;
    @BindView(R.id.create_form_description)
    EditText description;
    ScheduleViewModel model;

    private ScheduledItem item;
    private boolean creation;

    private void populateForm() {
        if (creation)
            return;

        this.artist.setText(item.getArtist(), TextView.BufferType.EDITABLE);
        this.genre.setText(item.getGenre(), TextView.BufferType.EDITABLE);
        this.room.setText(item.getItemLocation(), TextView.BufferType.EDITABLE);
        this.description.setText(item.getDescription(), TextView.BufferType.EDITABLE);
        this.beginDate.setText(DateUtils.formatDate(item.getJavaDate()));
        this.beginTime.setText(DateUtils.formatTime(item.getJavaDate()));
        this.duration.setText(DateUtils.formatDuration(item.getDuration()));
    }

    private void populateItem() {
        this.item.setArtist(getFieldValue(this.artist));
        this.item.setGenre(getFieldValue(this.genre));
        this.item.setItemLocation(getFieldValue(this.room));
        this.item.setDescription(getFieldValue(this.description));
        this.item.setDate(DateUtils.getDateValue(this.beginDate) + DateUtils.getTimeValue(this.beginTime));
        this.item.setDuration(DateUtils.getDurationValue(this.duration));
    }

    private boolean ensureLength(EditText field, int minLen, String name) {
        String value = getFieldValue(field);

        if (value == null) {
            Toast.makeText(getActivity(), getText(R.string.create_schedule_item_value_missing) + ": " + name, Toast.LENGTH_LONG).show();
            return false;
        }

        if (value.length() < minLen) {
            Toast.makeText(getActivity(), getText(R.string.create_schedule_item_value_length) + ": " + name + " (min " + minLen + " char)", Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }

    private boolean checkForm() {
        return ensureLength(artist, 1, "Artist")
                && ensureLength(genre, 1, "Type")
                && ensureLength(room, 1, "Room")
                && ensureLength(description, 10, "Description")
                && ensureLength(beginDate, 1, "Begin time")
                && ensureLength(duration, 1, "Duration");
    }

    private String getFieldValue(EditText field) {
        String s = field.getText().toString().trim();

        return s.isEmpty() ? null : s;
    }

    private void setupButton() {
        this.sendButton.setOnClickListener(v -> {
            if (!checkForm()) {
                return; // Cannot process if the form is not valid
            }

            this.sendButton.setEnabled(false);

            populateItem(); // Update the scheduled item with user values

            model.updateOrCreateScheduledItem(item).addOnSuccessListener(item -> {
                Log.i(TAG, "ScheduleItem created " + item);
                ((MultiFragmentActivity) requireActivity()).changeFragment(AdminScheduleParentFragment.newInstance(item.getItemLocation()), false, true);
            }).addOnFailureListener(failure -> {
                Log.e(TAG, "Schedule item creation or update failure for " + item + ": ", failure);
                this.sendButton.setEnabled(true);
                Toast.makeText(requireActivity(), getText(R.string.create_event_error) + failure.getMessage(), Toast.LENGTH_LONG).show();
            }).addOnCanceledListener(() -> {
                Log.e(TAG, "ScheduleItem creation cancelled" + item);
                Toast.makeText(requireActivity(), R.string.create_event_canceled, Toast.LENGTH_LONG).show();
                this.sendButton.setEnabled(true);
            });
        });
    }

    @OnClick(R.id.create_form_begin_date)
    void onClickBeginDate() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener listener = (view, year, month, dayOfMonth) -> {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            beginDate.setText(DateUtils.formatDate(calendar.getTime()));
        };
        DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(), listener,
                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000L);
        datePickerDialog.show();
    }

    @OnClick(R.id.create_form_duration)
    void onClickDuration() {
        TimePickerDialog dialog = new TimePickerDialog(requireContext(), (view, hourOfDay, minute) -> {
            duration.setText(DateUtils.formatTime(hourOfDay, minute, " h "));
        }, 0, 0, true);

        dialog.show();
    }

    @OnClick(R.id.create_form_begin_time)
    void onClickBeginTime() {
        TimePickerDialog dialog = new TimePickerDialog(requireContext(), (view, hourOfDay, minute) -> {
            beginTime.setText(DateUtils.formatTime(hourOfDay, minute, ":"));
        }, 0, 0, true);

        dialog.show();
    }

    public static Fragment newInstance(ScheduledItem item) {
        if(item == null) {
            throw new IllegalArgumentException("item cannot be null");
        }
        Bundle args = new Bundle();
        args.putParcelable(ScheduleEditFragment.SCHEDULE_ITEM, item);
        ScheduleEditFragment fragment = new ScheduleEditFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public ScheduleEditFragment() {
        this.creation = true;
        this.item = new ScheduledItem();
    }

    @Override
    public void onResume() {
        super.onResume();

        if (model == null) {
            model = ViewModelProviders.of(requireActivity()).get(ScheduleViewModel.class);
        }

        if (creation) {
            // Creation fragment: reset the content
            this.item = new ScheduledItem();
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //get ScheduledItem if non null
        if (getArguments() != null) {
            this.item = getArguments().getParcelable(SCHEDULE_ITEM);
            this.creation = false;
        }
        //Create view and bindings
        View view = inflater.inflate(R.layout.fragment_schedule_edit, container, false);
        ButterKnife.bind(this, view);

        this.setupButton();
        this.populateForm();

        return view;
    }

}
