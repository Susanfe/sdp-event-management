package ch.epfl.sweng.eventmanager.ui.CustomViews;

import android.app.Dialog;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ListAdapter;

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
                R.style.CustomDialog_TransparentBackground);

        builder.setView(R.layout.contextual_menu);

        setTypeAdapter();

        builder.setNegativeButton("Done", (dialog, which) -> {
            // Dismisses the Dialog
        });

        return builder.create();
    }

    private void setTypeAdapter() {
        ExpandableListView expandableListView = Objects.requireNonNull(getActivity())
                .findViewById(R.id.contextual_menu_type_options);
        expandableListView.setAdapter(new CustomExpendableListAdapter());
    }
}
