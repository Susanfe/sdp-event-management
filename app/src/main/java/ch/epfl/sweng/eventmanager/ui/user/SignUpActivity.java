package ch.epfl.sweng.eventmanager.ui.user;

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

public class SignUpActivity extends AppCompatActivity {
    private static final String TAG = "SignUpActivity";

    // UI references.
    @BindView(R.id.activity_login_email_field)
    EditText mEmailView;
    @BindView(R.id.activity_login_password_field)
    EditText mPasswordView;
    @BindView(R.id.password_confirmation_field)
    EditText mPasswordConfirmationView;
    @BindView(R.id.activity_login_signup_button)
    Button mSignUpButton;
    @BindView(R.id.activity_login_progress_bar)
    ProgressBar mProgressBar;
    @BindView(R.id.signup_toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);

        setupFieldsAndButton();

        mProgressBar.setVisibility(View.INVISIBLE);
        setSupportActionBar(toolbar);
    }

    private void setupFieldsAndButton() {
        mEmailView.setOnEditorActionListener(UserManagerHelper.nextButtonHandler(mEmailView));
        mPasswordView.setOnEditorActionListener(UserManagerHelper.nextButtonHandler(mPasswordView));
        mPasswordConfirmationView.setOnEditorActionListener(UserManagerHelper.nextButtonHandler(mPasswordConfirmationView));
        mSignUpButton.setOnClickListener(view -> attemptSignUp());
    }

    private void attemptSignUp() {
        Pair<Pair<Boolean, EditText>, Pair<String, String>> validatedForm = UserManagerHelper.validateForm(this,
                mEmailView, mPasswordView, mPasswordConfirmationView);

        // FIXME: Quite ugly, do we have a sexier way to return from UserManagerHelper.validateForm/3 ?
        boolean cancel = validatedForm.first.first;
        EditText focusView = validatedForm.first.second;
        String email = validatedForm.second.first;
        String password = validatedForm.second.second;

        if (cancel) {
            focusView.requestFocus();
        } else {
            UserManagerHelper.showProgress(mSignUpButton, mProgressBar, true);
            OnCompleteListener callback = UserManagerHelper.getAuthOnCompleteListener(this, mPasswordView,
                    mSignUpButton, mProgressBar);
            Session.registerAndLogin(email, password, this, callback);
        }
    }
}
