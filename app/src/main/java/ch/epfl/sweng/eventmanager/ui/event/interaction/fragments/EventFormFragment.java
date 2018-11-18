package ch.epfl.sweng.eventmanager.ui.event.interaction.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import ch.epfl.sweng.eventmanager.R;

public class EventFormFragment extends AbstractShowcaseFragment {

    /**
     * Holds the MIME data to specify what data is sent alongside ACTION_SEND tag
     */
    private static final String MIME_DATA = "message/rfc822";

    String email;

    @BindView(R.id.contact_form_send_button)
    Button sendButton;
    @BindView(R.id.contact_form_name)
    EditText name;
    @BindView(R.id.contact_form_subject)
    EditText subject;
    @BindView(R.id.contact_form_content)
    EditText content;

    public EventFormFragment() {
        super(R.layout.fragment_event_form);
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_event_form, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        model.getEvent().observe(this, ev -> {
            email = ev.getOrganizer().getEmail();
            // TODO handle NullPointerException

            name.setOnEditorActionListener((v, actionId, event) -> checkIfNext(actionId));

            subject.setOnEditorActionListener((v, actionId, event) -> checkIfNext(actionId));

            sendButton.setOnClickListener(v -> {
                String s_name = name.getText().toString();
                String s_subject = subject.getText().toString();
                String s_content = content.getText().toString();

                if (checkEmptyField(name, s_name) &&
                        checkEmptyField(subject, s_subject) &&
                        checkEmptyField(content, s_content)) {

                    // Sends an email
                    Intent i = createEmailIntent(s_name, s_subject, s_content);

                    try {
                        startActivityForResult(Intent.createChooser(i,
                                getActivity().getResources().getString(R.string.contact_form_choose_mail)),
                                Activity.RESULT_OK);
                        // TODO handle getResources return nullPointerException

                    } catch (android.content.ActivityNotFoundException ex) {
                        Toast.makeText(getActivity(),
                                getActivity().getResources().getString(R.string.contact_form_no_email_clients),
                                Toast.LENGTH_SHORT).show();
                    }
                }
            });
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Activity.RESULT_OK) getActivity().onBackPressed();
        // TODO handle NullPointerException
    }

    private boolean checkEmptyField(EditText v, String s_from_v) {
        if (s_from_v.trim().isEmpty()) {
            v.setError(getString(R.string.contact_form_error));
            v.requestFocus();
            return false;
        } else return true;

    }

    private boolean checkIfNext(int actionId) {
        if (actionId == EditorInfo.IME_ACTION_NEXT) {
            subject.requestFocus();
            return true;
        }
        return false;
    }

    private Intent createEmailIntent(String s_name, String s_subject, String s_content) {
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType(MIME_DATA); //email MIME data
        i.putExtra(Intent.EXTRA_EMAIL  , new String[] {email});
        i.putExtra(Intent.EXTRA_SUBJECT, s_name + " : " + s_subject);
        i.putExtra(Intent.EXTRA_TEXT   , s_content);
        return i;
    }
}

