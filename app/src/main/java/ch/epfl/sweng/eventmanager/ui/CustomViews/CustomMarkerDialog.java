package ch.epfl.sweng.eventmanager.ui.CustomViews;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.ArrayAdapter;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.fragment.app.DialogFragment;
import ch.epfl.sweng.eventmanager.R;
import ch.epfl.sweng.eventmanager.repository.data.SpotType;

public class CustomMarkerDialog extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(
                Objects.requireNonNull(getActivity()),
                R.style.CustomDialog_TransparentBackground);

        builder.setView(R.layout.contextual_menu);

        builder.setNegativeButton(getText(R.string.done), (dialog, which) -> {
            // Dismisses the Dialog
        });

        return builder.create();
    }

    @Override
    public void onResume() {
        super.onResume();

        setTypeAdapter(getContext());
    }

    private void setTypeAdapter(Context context) {
        AppCompatSpinner typeSpinner = getDialog().findViewById(R.id.contextual_menu_spotType_spinner);

        ArrayAdapter<SpotType> adapter = new ArrayAdapter<>(context,
                android.R.layout.simple_spinner_item,
                SpotType.values());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        typeSpinner.setAdapter(adapter);

    }
}
