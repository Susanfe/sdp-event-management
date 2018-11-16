package ch.epfl.sweng.eventmanager.ui.userManager;

import android.content.Context;
import android.content.Intent;
import androidx.core.util.Pair;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.AuthResult;

import ch.epfl.sweng.eventmanager.R;
import ch.epfl.sweng.eventmanager.ui.eventSelector.EventPickingActivity;

public class UserManagerHelper {
    private UserManagerHelper() {
        // Used to block instantiation. Empty on purpose.
    }

    /**
     * When the next button is clicked on the keyboard, move to the next field.
     */
    public static TextView.OnEditorActionListener nextButtonHandler(EditText field) {
        return (v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_NEXT) {
                field.requestFocus();
                return true;
            }
            return false;
        };
    }

    /**
     * Check whether the format of an email address is 'good enough'.
     *
     * @param email address to validate
     * @return true if 'valid', false otherwise
     */
    public static boolean isEmailValid(String email) {
        // FIXME: run a proper regex on the given email.
        return email.contains("@");
    }

    /**
     * Toggle 'in progress' view on sign in/up forms while querying the authentication backend.
     *
     * @param loginButton button to be disable or enable
     * @param progressBar progress bar to disable or enable
     * @param displayed state of the 'in progress' view
     */
    public static void showProgress(Button loginButton, ProgressBar progressBar, boolean displayed) {
        loginButton.setEnabled(!displayed);
        progressBar.setVisibility(displayed ? View.VISIBLE : View.INVISIBLE);
    }

    /**
     * Validate the content of a sign in/up form.
     *
     * @param context calling activity
     * @param emailView email text field
     * @param passwordView password text field
     * @param passwordConfirmationView password confirmation text field, may be null
     * @return A pair containing the decided actions and the extracted values
     */
    public static Pair<Pair<Boolean, EditText>, Pair<String, String>> validateForm
            (Context context, EditText emailView, EditText passwordView, EditText passwordConfirmationView) {

        // Reset errors.
        emailView.setError(null);
        passwordView.setError(null);

        boolean cancel = false;
        View focusView = null;

        String email = emailView.getText().toString();
        String password = passwordView.getText().toString();
        String passwordConfirmation;

        // Validate input from login form.
        if (passwordConfirmationView != null) {
            passwordConfirmation = passwordConfirmationView.getText().toString();
            if (!password.equals(passwordConfirmation)) {
                passwordView.setError(context.getString(R.string.password_match_error));
                focusView = passwordView;
                cancel = true;
            }
        }

        if (TextUtils.isEmpty(password)) {
            passwordView.setError(context.getString(R.string.empty_password_error));
            focusView = passwordView;
            cancel = true;
        }

        if (TextUtils.isEmpty(email) || !UserManagerHelper.isEmailValid(email)) {
            emailView.setError(context.getString(R.string.invalid_email_error));
            focusView = emailView;
            cancel = true;
        }

        Pair<Boolean, EditText> state = new Pair(cancel, focusView);
        Pair<String, String> values = new Pair(email, password);

        return new Pair(state, values);
    }

    /**
     * Generate the callback to be executed when the authentication process exits.
     *
     * @param context calling activity
     * @param passwordView password text field, used to set error messages
     * @param signUpButton button to re-enable when the auth process exits
     * @param progressBar progress bar to disable when the auth process exits
     * @return the generated callback
     */
    public static OnCompleteListener<AuthResult> getAuthOnCompleteListener(
            Context context, EditText passwordView, Button signUpButton, ProgressBar progressBar) {
        return task -> {
            if (task.isSuccessful()) {
                Intent intent = new Intent(context, EventPickingActivity.class);
                context.startActivity(intent);

                String toastMessage = "";
                if (context instanceof SignUpActivity) {
                    toastMessage = context.getString(R.string.successful_registration_toast);
                } else if (context instanceof LoginActivity) {
                    toastMessage = context.getString(R.string.successful_login_toast);
                }

                Toast toast = Toast.makeText(context, toastMessage, Toast.LENGTH_SHORT);
                toast.show();
            } else {
                Exception error = task.getException();
                if (error != null) {
                    passwordView.setError(error.getMessage());
                } else {
                    passwordView.setError(context.getString(R.string.generic_auth_error));
                }
                passwordView.requestFocus();
            }

            UserManagerHelper.showProgress(signUpButton, progressBar,false);
        };
    }
}
