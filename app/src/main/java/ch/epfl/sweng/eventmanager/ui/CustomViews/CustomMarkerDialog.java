package ch.epfl.sweng.eventmanager.ui.CustomViews;

import android.app.Dialog;
import android.os.Bundle;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import ch.epfl.sweng.eventmanager.R;

public class CustomMarkerDialog extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(
                Objects.requireNonNull(getActivity()),
                R.style.Theme_MaterialComponents_Dialog_Alert);

        builder.setView(R.layout.contextual_menu);

        return builder.create();
    }
}
