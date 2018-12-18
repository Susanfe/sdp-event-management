package ch.epfl.sweng.eventmanager.ui.CustomViews;

import android.app.Dialog;
import android.os.Bundle;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import ch.epfl.sweng.eventmanager.R;

public class CustomInfoDialog extends DialogFragment {

    public static final String CUSTOM_DIALOG_INFO_STRING = "ui.customViews.CUSTOM_DIALOG_INFO_STRING";
    private static final String DEFAULT_INFO = "No info";
    private String info;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(
                Objects.requireNonNull(getActivity()),
                R.style.CustomDialog_TransparentBackground);

        Bundle bundle = getArguments();

        assert bundle!=null;
        info = bundle.getString(CUSTOM_DIALOG_INFO_STRING);

        if (info == null)
            builder.setMessage(DEFAULT_INFO);
        else
            builder.setMessage(info);

        // Quit dialog on OK button click
        builder.setNeutralButton(R.string.ok, (dialog, which) -> {});

        return builder.create();
    }
}
