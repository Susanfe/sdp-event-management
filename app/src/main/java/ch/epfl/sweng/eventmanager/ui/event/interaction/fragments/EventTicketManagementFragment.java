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

import java.io.File;
import java.nio.charset.Charset;

import butterknife.BindView;
import butterknife.ButterKnife;
import ch.epfl.sweng.eventmanager.R;

import static android.app.Activity.RESULT_OK;

/**
 * Allows an administrator a manage the users allowed to work with this event (= access to admin
 * features).
 */
public class EventTicketManagementFragment extends AbstractShowcaseFragment {
    private final int ACTIVITY_CHOOSE_FILE = 1;
    private static final String TAG = "EventTicketManagement";

    @BindView(R.id.select_file_button)
    Button selectFileButton;

    @BindView(R.id.file_path_field)
    EditText filePathField;

    @BindView(R.id.upload_button)
    Button uploadButton;

    public EventTicketManagementFragment() {
        // Required empty public constructor
        super(R.layout.fragment_event_ticket_management);
    }

    @Override
    public void onResume() {
        super.onResume();

        model.getEvent().observe(this, ev -> {
            if (ev == null) {
                Log.e(TAG, "Got null model from parent activity");
            }

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
            chooseFile.setType("text/plain");
            intent = Intent.createChooser(chooseFile, "Choose CSV file");
            startActivityForResult(intent, ACTIVITY_CHOOSE_FILE);
        });

        uploadButton.setText(getString(R.string.upload_button));
        uploadButton.setOnClickListener(v -> {
            try {
                String filepath = filePathField.getText().toString();
                File rawData = new File(filepath);
                CSVParser parser = CSVParser.parse(rawData, Charset.defaultCharset(), CSVFormat.RFC4180);
            } catch (Exception e) {
                Toast error = Toast.makeText(getActivity(), "Could not read file: " + e.toString(), Toast.LENGTH_LONG);
                error.show();
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode) {
            case ACTIVITY_CHOOSE_FILE: {
                if (resultCode == RESULT_OK){
                    Uri uri = data.getData();
                    String filePath = uri.getPath();
                    filePathField.setText(filePath);
                }
            }
        }
    }
}
