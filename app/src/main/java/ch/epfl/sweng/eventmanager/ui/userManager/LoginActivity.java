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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.AuthResult;

import ch.epfl.sweng.eventmanager.R;
import ch.epfl.sweng.eventmanager.users.Session;
import dagger.android.AndroidInjection;

import javax.inject.Inject;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";

    // UI references.
    private EditText mEmailView;
    private EditText mPasswordView;
    private Button mLoginButton;
    private ProgressBar mProgressBar;

    @Inject
    Session session;

    private void setupFields() {
        mEmailView = findViewById(R.id.email_field);
        mEmailView.setHint(R.string.email_field);

        // When the next button is clicked on the keyboard, move to the next field
        mEmailView.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_NEXT) {
                mPasswordView.requestFocus();
                return true;
            }
            return false;
        });

        mPasswordView = findViewById(R.id.password_field);
        mPasswordView.setHint(R.string.password_field);

        // When the done button is clicked on the keyboard, try to login
        mPasswordView.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                attemptLogin();
                return true;
            }
            return false;
        });
    }

    private void setupButton() {
        mLoginButton = findViewById(R.id.login_button);
        mLoginButton.setText(R.string.login_button);
        mLoginButton.setOnClickListener(view -> attemptLogin());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize UI
        setupFields();
        setupButton();

        mProgressBar = findViewById(R.id.sign_in_progress_bar);
        mProgressBar.setVisibility(View.INVISIBLE);

        Toolbar toolbar = findViewById(R.id.login_toolbar);
        setSupportActionBar(toolbar);
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
        if (TextUtils.isEmpty(email) || !isEmailValid(email)) {
            mEmailView.setError(getString(R.string.invalid_email_activity_login));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            showProgress(true);
            session.login(email, password, this, getSignInOnCompleteListener(this));
        }
    }

    private OnCompleteListener<AuthResult> getSignInOnCompleteListener(Context mContext) {
        return task -> {
            if (task.isSuccessful()) {
                Log.d(TAG, "Successful sign in");
                Intent intent = new Intent(mContext, DisplayAccountActivity.class);
                startActivity(intent);
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

            showProgress(false);
        };
    }

    private boolean isEmailValid(String email) {
        // FIXME: run a proper regex on the given email.
        return email.contains("@");
    }

    private void showProgress(boolean displayed) {
        mLoginButton.setEnabled(!displayed);
        mProgressBar.setVisibility(displayed ? View.VISIBLE : View.INVISIBLE);
    }
}


