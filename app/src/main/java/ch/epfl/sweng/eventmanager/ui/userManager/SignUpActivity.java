package ch.epfl.sweng.eventmanager.ui.userManager;

import androidx.core.util.Pair;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;


import com.google.android.gms.tasks.OnCompleteListener;

import ch.epfl.sweng.eventmanager.R;
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

        Toolbar toolbar = findViewById(R.id.signup_toolbar);
        setSupportActionBar(toolbar);
    }

    private void setupFields() {
        mEmailView = findViewById(R.id.email_field);
        mEmailView.setHint(R.string.email_field);
        mEmailView.setOnEditorActionListener(UserManagerHelper.nextButtonHandler(mEmailView));

        mPasswordView = findViewById(R.id.password_field);
        mPasswordView.setHint(R.string.password_field);
        mPasswordView.setOnEditorActionListener(UserManagerHelper.nextButtonHandler(mPasswordView));

        mPasswordConfirmationView = findViewById(R.id.password_confirmation_field);
        mPasswordConfirmationView.setHint(R.string.password_confirmation_field);
        mPasswordConfirmationView.setOnEditorActionListener(
                UserManagerHelper.nextButtonHandler(mPasswordConfirmationView)
        );
    }

    private void setupButtons() {
        mSignUpButton = findViewById(R.id.signup_button);
        mSignUpButton.setText(R.string.signup_button);
        mSignUpButton.setOnClickListener(view -> attemptSignUp());
    }

    private void attemptSignUp() {
        Pair<Pair<Boolean, EditText>, Pair<String, String>> validatedForm
                = UserManagerHelper.validateForm(this, mEmailView, mPasswordView, mPasswordConfirmationView);

        // FIXME: Quite ugly, do we have a sexier way to return from UserManagerHelper.validateForm/3 ?
        Boolean cancel = validatedForm.first.first;
        EditText focusView = validatedForm.first.second;
        String email = validatedForm.second.first;
        String password = validatedForm.second.second;

        if (cancel) {
            focusView.requestFocus();
        } else {
            UserManagerHelper.showProgress(mSignUpButton, mProgressBar, true);
            OnCompleteListener callback = UserManagerHelper.getAuthOnCompleteListener(
                    this, mPasswordView, mSignUpButton, mProgressBar
            );
            Session.registerAndLogin(email, password, this, callback);
        }
    }
}
