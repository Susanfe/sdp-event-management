package ch.epfl.sweng.eventmanager.ui.ticketing;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import ch.epfl.sweng.eventmanager.R;
import ch.epfl.sweng.eventmanager.ticketing.TicketingService;
import ch.epfl.sweng.eventmanager.ticketing.data.ApiResult;
import dagger.android.AndroidInjection;

import java.util.List;

/**
 * A login screen that offers login via email/password.
 */
public final class TicketingLoginActivity extends TicketingActivity {
    // UI references.
    private EditText mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;

    public TicketingLoginActivity() {
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        AndroidInjection.inject(this);

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_ticketing_login);

        // Set up the login form.
        mEmailView = findViewById(R.id.email);

        mPasswordView = findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener((textView, id, keyEvent) -> {
            if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                attemptLogin();
                return true;
            }
            return false;
        });

        Button mEmailSignInButton = findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(view1 -> attemptLogin());

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }

    private boolean checkFieldInvalid(EditText textView, Validator isValid) {
        String val = textView.getText().toString();

        if (TextUtils.isEmpty(val)) {
            textView.setError(getString(R.string.error_field_required));
            textView.requestFocus();
            return true;
        } else if (!isValid.apply(val)) {
            textView.setError(getString(R.string.error_field_invalid));
            textView.requestFocus();
            return true;
        }

        return false;
    }

    public static interface Validator {
        // Replaces Function<String,Boolean> that is not available at this API level
        boolean apply(String value);
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);


        boolean cancel =
                service == null ||
                        checkFieldInvalid(mEmailView, this::isEmailValid) ||
                        checkFieldInvalid(mPasswordView, this::isPasswordValid);

        if (!cancel) {
            // Store values at the time of the login attempt.
            String email = mEmailView.getText().toString();
            String password = mPasswordView.getText().toString();

            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);

            service.login(email, password, new TicketingService.ApiCallback<Void>() {
                @Override
                public void onSuccess(Void data) {
                    Intent openIntent = switchActivity(getNextActivityForState(service));
                    startActivity(openIntent);

                    finish();
                }

                @Override
                public void onFailure(List<ApiResult.ApiError> errors) {
                    // TODO: parse login errors
                    Toast.makeText(TicketingLoginActivity.this, "Erreur de connexion", Toast.LENGTH_LONG).show();
                    showProgress(false);
                }
            });
        } else if (service == null) {
            Toast.makeText(this, R.string.ticketing_missing_service, Toast.LENGTH_LONG).show();
        }
    }

    private boolean isEmailValid(String email) {
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 4;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

        mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            }
        });

        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        mProgressView.animate().setDuration(shortAnimTime).alpha(
                show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            }
        });
    }

}

