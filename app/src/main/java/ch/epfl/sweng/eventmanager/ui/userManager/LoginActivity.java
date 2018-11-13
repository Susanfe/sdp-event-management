package ch.epfl.sweng.eventmanager.ui.userManager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.AuthResult;

import java.text.Normalizer;

import ch.epfl.sweng.eventmanager.R;
import ch.epfl.sweng.eventmanager.users.Session;
import dagger.android.AndroidInjection;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";

    // UI references.
    private EditText mEmailView;
    private EditText mPasswordView;
    private Button mLoginButton;
    private Button mSignUpButton;
    private ProgressBar mProgressBar;

    private void setupFields() {
        mEmailView = findViewById(R.id.email_field);
        mEmailView.setHint(R.string.email_field);
        mEmailView.setOnEditorActionListener(FormHelper.nextButtonHandler(mEmailView));

        mPasswordView = findViewById(R.id.password_field);
        mPasswordView.setHint(R.string.password_field);

        // When the done button is clicked on the keyboard, try to login
        mPasswordView.setOnEditorActionListener(FormHelper.nextButtonHandler(mPasswordView));
    }

    private void setupButtons() {
        mLoginButton = findViewById(R.id.login_button);
        mLoginButton.setText(R.string.login_button);
        mLoginButton.setOnClickListener(view -> attemptLogin());

        mSignUpButton = findViewById(R.id.signup_button);
        mSignUpButton.setText(R.string.signup_button);
        mSignUpButton.setOnClickListener(view -> openSignUpForm());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize UI
        setupFields();
        setupButtons();

        mProgressBar = findViewById(R.id.sign_in_progress_bar);
        mProgressBar.setVisibility(View.INVISIBLE);

        Toolbar toolbar = findViewById(R.id.login_toolbar);
        setSupportActionBar(toolbar);
    }

    private void openSignUpForm() {
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
    }

    private void attemptLogin() {
        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Validate input from login form.
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
            FormHelper.showProgress(mLoginButton, mProgressBar,true);
            Session.login(email, password, this, getSignInOnCompleteListener(this));
        }
    }

    private OnCompleteListener<AuthResult> getSignInOnCompleteListener(Context mContext) {
        return task -> {
            if (task.isSuccessful()) {
                Log.d(TAG, "Successful sign in");
                Intent intent = new Intent(mContext, DisplayAccountActivity.class);
                startActivity(intent);

                Toast toast = Toast.makeText(
                        this, getString(R.string.successful_login_toast), Toast.LENGTH_SHORT
                );
                toast.show();
            } else {
                Exception error = task.getException();
                Log.w(TAG, "Sign in failed", task.getException());
                if (error != null) {
                    mPasswordView.setError(error.getMessage());
                } else {
                    mPasswordView.setError(getString(R.string.generic_signin_failure_activity_login));
                }
                mPasswordView.requestFocus();
            }

            FormHelper.showProgress(mLoginButton, mProgressBar,false);
        };
    }
}


