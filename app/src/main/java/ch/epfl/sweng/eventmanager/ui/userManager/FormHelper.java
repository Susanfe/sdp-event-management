package ch.epfl.sweng.eventmanager.ui.userManager;

import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

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
}
