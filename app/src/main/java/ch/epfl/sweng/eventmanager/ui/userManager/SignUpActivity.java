package ch.epfl.sweng.eventmanager.ui.userManager;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;


import ch.epfl.sweng.eventmanager.R;

public class SignUpActivity extends AppCompatActivity {

    // UI references.
    private EditText mEmailView;
    private EditText mPasswordView;
    private EditText mPasswordConfirmationView;
    private Button mLoginButton;
    private Button mSignUpButton;
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        setupFields();
        setupButtons();
    }

    private void setupFields() {
        mEmailView = findViewById(R.id.email_field);
        mEmailView.setHint(R.string.email_field);
        mEmailView.setOnEditorActionListener(FormHelper.nextButtonHandler(mEmailView));

        mPasswordView = findViewById(R.id.password_field);
        mPasswordView.setHint(R.string.password_field);
        mPasswordView.setOnEditorActionListener(FormHelper.nextButtonHandler(mPasswordView));

        mPasswordConfirmationView = findViewById(R.id.password_confirmation_field);
        mPasswordConfirmationView.setHint(R.string.password_confirmation_field);
        mPasswordConfirmationView.setOnEditorActionListener(
                FormHelper.nextButtonHandler(mPasswordConfirmationView)
        );
    }

    private void setupButtons() {
        mSignUpButton = findViewById(R.id.signup_button);
        mSignUpButton.setText(R.string.signup_button);
        mSignUpButton.setOnClickListener(view -> attemptSignUp());
    }

    private void attemptSignUp() {
        // TODO
    }
}
