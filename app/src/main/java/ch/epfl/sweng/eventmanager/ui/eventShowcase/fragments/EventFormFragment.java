package ch.epfl.sweng.eventmanager.ui.eventShowcase.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
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

    String email;

    private final int REQUEST_CODE = 1;

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
            email = ev.getEmail();
            // TODO handle NullPointerException

            name.setOnEditorActionListener((v, actionId, event) -> {
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    subject.requestFocus();
                    return true;
                }
                return false;
            });

            subject.setOnEditorActionListener((v, actionId, event) -> {
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    content.requestFocus();
                    return true;
                }
                return false;
            });

            sendButton.setOnClickListener(v -> {
                String s_name = name.getText().toString();
                String s_subject = subject.getText().toString();
                String s_content = content.getText().toString();

                if (checkEmptyField(name, s_name) &&
                        checkEmptyField(subject, s_subject) &&
                        checkEmptyField(content, s_content)) {

                    // Sends an email
                    Intent i = new Intent(Intent.ACTION_SEND);
                    i.setType("message/rfc822"); //email MIME data
                    i.putExtra(Intent.EXTRA_EMAIL  , email);
                    i.putExtra(Intent.EXTRA_SUBJECT, s_name + " : " + s_subject);
                    i.putExtra(Intent.EXTRA_TEXT   , s_content);

                    try {
                        startActivityForResult(Intent.createChooser(i,
                                getActivity().getResources().getString(R.string.contact_form_choose_mail)), REQUEST_CODE);
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
        if (requestCode == REQUEST_CODE) getActivity().onBackPressed();
        // TODO handle NullPointerException
    }

    private boolean checkEmptyField(EditText v, String s_from_v) {
        if (s_from_v.trim().isEmpty()) {
            v.setError(getString(R.string.contact_form_empty_field));
            return false;
        } else return true;

    }
}

