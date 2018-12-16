package ch.epfl.sweng.eventmanager.ui.event.interaction.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.File;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import ch.epfl.sweng.eventmanager.FileUtil;
import ch.epfl.sweng.eventmanager.R;
import ch.epfl.sweng.eventmanager.repository.CloudFunction;
import ch.epfl.sweng.eventmanager.repository.data.Event;
import ch.epfl.sweng.eventmanager.repository.data.Ticket;
import dagger.android.support.AndroidSupportInjection;

import static android.app.Activity.RESULT_OK;

/**
 * Allows an administrator a manage the users allowed to work with this event (= access to admin
 * features).
 */
public class EventTicketManagementFragment extends AbstractShowcaseFragment {
    private final int ACTIVITY_CHOOSE_FILE = 1;
    private static final String TAG = "EventTicketManagement";
    private File mFile = null;

    @BindView(R.id.select_file_button)
    Button selectFileButton;

    @BindView(R.id.file_path_field)
    EditText filePathField;

    @BindView(R.id.upload_button)
    Button uploadButton;

    @Inject
    CloudFunction cloudFunction;

    public EventTicketManagementFragment() {
        // Required empty public constructor
        super(R.layout.fragment_event_ticket_management);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        AndroidSupportInjection.inject(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();

        model.getEvent().observe(this, ev -> {
            if (ev == null) {
                Log.e(TAG, "Got null model from parent activity");
            }

            uploadButton.setOnClickListener(v -> {
                setInProgressState(true);
                try {
                    CSVParser parser = CSVParser.parse(mFile, Charset.defaultCharset(), CSVFormat.RFC4180);
                    importFromRecords(parser.getRecords(), ev);
                } catch (Exception e) {
                    Toast error = Toast.makeText(getActivity(), "Could not parse file: " + e.toString(), Toast.LENGTH_LONG);
                    setInProgressState(false);
                    error.show();
                }
            });
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, view);

        selectFileButton.setText(getString(R.string.select_file_button));
        selectFileButton.setOnClickListener(v -> {
            Intent chooseFile;
            Intent intent;
            chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
            chooseFile.setType("text/*");
            intent = Intent.createChooser(chooseFile, "Choose CSV file");
            startActivityForResult(intent, ACTIVITY_CHOOSE_FILE);
        });

        uploadButton.setText(getString(R.string.upload_button));

        return view;
    }

    void setInProgressState(Boolean working) {
        if (working) uploadButton.setEnabled(false);
        else uploadButton.setEnabled(true);
    }

    void importFromRecords(List<CSVRecord> rawRecords, Event event) {
        List<Ticket> ticketList = new ArrayList<>();
        for (CSVRecord r: rawRecords) {
            String id = r.get(0);
            String name = r.get(1);
            if (id != null) { // Found a 'valid' entry
                Ticket ticket = new Ticket(id, name);
                ticketList.add(ticket);
            }
        }

        // Call FB cloud function to store in FB realtime db
        cloudFunction.importTickets(ticketList, event.getId())
                .addOnCompleteListener(task -> {
                    String toastText;
                    if (task.isSuccessful()) toastText = getString(R.string.tickets_upload_success);
                    else toastText = getString(R.string.tickets_upload_failure);
                    Toast.makeText(getActivity(), toastText, Toast.LENGTH_LONG).show();
                    setInProgressState(false);
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode) {
            case ACTIVITY_CHOOSE_FILE: {
                if (resultCode == RESULT_OK){
                    Uri uri = data.getData();
                    try {
                        mFile = FileUtil.from(getActivity(), uri);
                        filePathField.setText(mFile.getPath());
                    } catch (Exception e) {
                        Toast error = Toast.makeText(getActivity(), "Could not resolve file: " + e.toString(), Toast.LENGTH_LONG);
                        error.show();
                    }
                }
            }
        }
    }
}
