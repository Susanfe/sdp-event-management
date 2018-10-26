package ch.epfl.sweng.eventmanager.ui.eventShowcase.fragments.schedule;

import android.arch.lifecycle.LiveData;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import ch.epfl.sweng.eventmanager.R;
import ch.epfl.sweng.eventmanager.data.ScheduledItem;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.List;

public class MyScheduleFragment extends AbstractScheduleFragment {
    private static final String TAG = "MyScheduleFragment";
    private static final String CALENDAR_FILE_NAME = "myschedule.ics";

    private Button addToCalendarButton;

    public MyScheduleFragment() {
        super(R.layout.activity_my_schedule);
    }

    @Override
    protected void setEmptyListTextView() {
        super.emptyListTextView.setText(R.string.my_schedule_empty);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, container, savedInstanceState);

        this.addToCalendarButton = v.findViewById(R.id.addToCalendar);
        this.addToCalendarButton.setOnClickListener(v1 -> {
            this.writeEventsToCalendar(this.getScheduledItems().getValue());
            this.openCalendar();
        });

        return v;
    }

    @Override
    protected void onItemsUpdate(List<ScheduledItem> items) {
        if (addToCalendarButton == null)
            return;

        if (items.size() > 0)
            addToCalendarButton.setVisibility(Button.VISIBLE);
        else
            addToCalendarButton.setVisibility(Button.INVISIBLE);
    }

    @Override
    protected LiveData<List<ScheduledItem>> getScheduledItems() {
        return this.model.getJoinedScheduleItems();
    }

    void writeEventsToCalendar(List<ScheduledItem> mySchedule) {
        FileOutputStream outputStream;

        try {
            outputStream = getContext().openFileOutput(CALENDAR_FILE_NAME, Context.MODE_PRIVATE);

            writeCalendar(mySchedule, outputStream);

            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void writeCalendar(List<ScheduledItem> events, OutputStream stream) {
        PrintStream printer = new PrintStream(stream);

        printer.println("BEGIN:VCALENDAR\n" +
                "VERSION:2.0\n" +
                "PRODID:-//EventManager/MySchedule//Event Schedule//EN");

        for (ScheduledItem item : events) {
            item.printAsIcalendar(printer);
        }

        printer.println("END:VCALENDAR");

        printer.close();
    }

    private void openCalendar() {
        Intent openFile = new Intent(Intent.ACTION_VIEW);
        Uri uri = FileProvider.getUriForFile(getContext(), "ch.epfl.sweng.eventmanager.fileprovider", new File(getContext().getFilesDir(), CALENDAR_FILE_NAME));
        openFile.setDataAndType(uri, "text/calendar");

        // https://developer.android.com/reference/android/support/v4/content/FileProvider#GetUri
        openFile.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        openFile.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);


        try {
            getContext().startActivity(openFile);
        } catch (ActivityNotFoundException e) {
            Log.i(TAG, "Cannot open file.");
        }
    }
}
