package ch.epfl.sweng.eventmanager.ui.userManager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.AuthResult;

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
        Pair<Pair<Boolean, EditText>, Pair<String, String>> validatedForm
                = FormHelper.validateForm(this, mEmailView, mPasswordView, null);

        // FIXME: Quite ugly, do we have a sexier way to return from FormHelper.validateForm/3 ?
        Boolean cancel = validatedForm.first.first;
        EditText focusView = validatedForm.first.second;
        String email = validatedForm.second.first;
        String password = validatedForm.second.second;

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
                    mPasswordView.setError(getString(R.string.generic_signin_failure_error));
                }
                mPasswordView.requestFocus();
            }

            FormHelper.showProgress(mLoginButton, mProgressBar,false);
        };
    }
}


