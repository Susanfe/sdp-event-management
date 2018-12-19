package ch.epfl.sweng.eventmanager.ui.user;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.util.Pair;
import butterknife.BindView;
import butterknife.ButterKnife;
import ch.epfl.sweng.eventmanager.R;
import ch.epfl.sweng.eventmanager.users.Session;
import com.google.android.gms.tasks.OnCompleteListener;
import dagger.android.AndroidInjection;

import javax.inject.Inject;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";

    // UI references.
    @BindView(R.id.activity_login_email_field)
    EditText mEmailView;
    @BindView(R.id.activity_login_password_field)
    EditText mPasswordView;
    @BindView(R.id.activity_login_login_button)
    Button mLoginButton;
    @BindView(R.id.activity_login_signup_button)
    Button mSignUpButton;
    @BindView(R.id.activity_login_progress_bar)
    ProgressBar mProgressBar;
    @BindView(R.id.activity_login_toolbar)
    Toolbar toolbar;

    @Inject
    Session session;

    private void setupFields() {
        mEmailView.setOnEditorActionListener(UserManagerHelper.nextButtonHandler(mEmailView));
        // When the done button is clicked on the keyboard, try to login
        mPasswordView.setOnEditorActionListener(UserManagerHelper.nextButtonHandler(mPasswordView));
    }

    private void setupButtons() {
        mLoginButton.setOnClickListener(view -> attemptLogin());
        mSignUpButton.setOnClickListener(view -> openSignUpForm());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        // Initialize UI
        setupFields();
        setupButtons();
        setSupportActionBar(toolbar);
        mProgressBar.setVisibility(View.INVISIBLE);
    }

    private void openSignUpForm() {
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
    }

    private void attemptLogin() {
        Pair<Pair<Boolean, EditText>, Pair<String, String>> validatedForm = UserManagerHelper.validateForm(this,
                mEmailView, mPasswordView, null);

        // FIXME: Quite ugly, do we have a sexier way to return from UserManagerHelper.validateForm/3 ?
        boolean cancel = false;
        EditText focusView = null;
        if (validatedForm.first != null) {
            if (validatedForm.first.first != null)
                cancel = validatedForm.first.first;

            focusView = validatedForm.first.second;
        }

        String email = "", password =  "";
        if (validatedForm.second != null) {
            email = validatedForm.second.first;
            password = validatedForm.second.second;
        }

        if (cancel) {
            assert focusView != null;
            focusView.requestFocus();
        } else {
            UserManagerHelper.showProgress(mLoginButton, mProgressBar, true);
            OnCompleteListener callback = UserManagerHelper.getAuthOnCompleteListener(this, mPasswordView,
                    mSignUpButton, mProgressBar);
            session.login(email, password, this, callback);
        }
    }
}
