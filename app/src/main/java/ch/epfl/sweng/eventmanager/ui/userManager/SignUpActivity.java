package ch.epfl.sweng.eventmanager.ui.userManager;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.AuthResult;

import ch.epfl.sweng.eventmanager.R;
import ch.epfl.sweng.eventmanager.ui.eventSelector.EventPickingActivity;
import ch.epfl.sweng.eventmanager.users.Session;

public class SignUpActivity extends AppCompatActivity {
    private static final String TAG = "SignUpActivity";

    // UI references.
    private EditText mEmailView;
    private EditText mPasswordView;
    private EditText mPasswordConfirmationView;
    private Button mSignUpButton;
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        setupFields();
        setupButtons();

        mProgressBar = findViewById(R.id.sign_in_progress_bar);
        mProgressBar.setVisibility(View.INVISIBLE);

        Toolbar toolbar = findViewById(R.id.login_toolbar);
        setSupportActionBar(toolbar);
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
        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the sign up attempt
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();
        String password_confirmation = mPasswordConfirmationView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Validate input from login form.
        if (!password.equals(password_confirmation)) {
            mPasswordView.setError(getString(R.string.password_match_error));
            focusView = mPasswordView;
            cancel = true;
        }

        if (TextUtils.isEmpty(password)) {
            mPasswordView.setError(getString(R.string.empty_password_activity_login));
            focusView = mPasswordView;
            cancel = true;
        }
        if (TextUtils.isEmpty(email) || !FormHelper.isEmailValid(email)) {
            mEmailView.setError(getString(R.string.invalid_email_activity_login));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            FormHelper.showProgress(mSignUpButton, mProgressBar, true);
            Session.registerAndLogin(email, password, this, getSignUpOnCompleteListener(this));
        }
    }

    private OnCompleteListener<AuthResult> getSignUpOnCompleteListener(Context mContext) {
        return task -> {
            if (task.isSuccessful()) {
                Log.d(TAG, "Successfully signed up");
                Intent intent = new Intent(mContext, EventPickingActivity.class);
                startActivity(intent);

                Toast toast = Toast.makeText(
                        this, getString(R.string.successful_registration_toast), Toast.LENGTH_SHORT
                );
                toast.show();
            } else {
                Exception error = task.getException();
                Log.w(TAG, "Registration failed", task.getException());
                if (error != null) {
                    mPasswordView.setError(error.getMessage());
                } else {
                    mPasswordView.setError(getString(R.string.generic_signup_failure_error));
                }
                mPasswordView.requestFocus();
            }

            FormHelper.showProgress(mSignUpButton, mProgressBar,false);
        };
    }
}
