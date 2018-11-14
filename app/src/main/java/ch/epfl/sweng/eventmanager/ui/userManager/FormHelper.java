package ch.epfl.sweng.eventmanager.ui.userManager;

import android.content.Context;
import android.content.res.Resources;
import android.support.v4.util.Pair;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import ch.epfl.sweng.eventmanager.R;

public class FormHelper {
    private FormHelper() {
        // Used to block instantiation. Empty on purpose.
    }

    /**
     * When the next button is clicked on the keyboard, move to the next field
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

    public static boolean isEmailValid(String email) {
        // FIXME: run a proper regex on the given email.
        return email.contains("@");
    }

    public static void showProgress(Button loginButton, ProgressBar progressBar, boolean displayed) {
        loginButton.setEnabled(!displayed);
        progressBar.setVisibility(displayed ? View.VISIBLE : View.INVISIBLE);
    }

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
            passwordView.setError(context.getString(R.string.empty_password_activity_login));
            focusView = passwordView;
            cancel = true;
        }

        if (TextUtils.isEmpty(email) || !FormHelper.isEmailValid(email)) {
            emailView.setError(context.getString(R.string.invalid_email_activity_login));
            focusView = emailView;
            cancel = true;
        }

        Pair<Boolean, EditText> state = new Pair(cancel, focusView);
        Pair<String, String> values = new Pair(email, password);

        return new Pair(state, values);
    }
}
