package ch.epfl.sweng.eventmanager.ui.CustomViews;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.EditText;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.fragment.app.DialogFragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import ch.epfl.sweng.eventmanager.R;
import ch.epfl.sweng.eventmanager.repository.data.SpotType;
import ch.epfl.sweng.eventmanager.ui.event.interaction.fragments.map.EventMapEditionFragment;

/**
 * Class used to interact with user when he wants to modify a marker in the MapEdition fragment
 * The dialog displays the current title, snippet and SpotType of the selected spot and allows user
 * to modify them.
 */
public class CustomMarkerDialog extends DialogFragment {

    public static final String EXTRA_TITLE = "ui.CustomViews.EXTRA_TITLE";
    public static final String EXTRA_SNIPPET = "ui.CustomViews.EXTRA_SNIPPET";
    public  static final String EXTRA_TYPE = "ui.CustomViews.EXTRA_TYPE";

    @BindView(R.id.contextual_edit_marker_title)
    EditText titleEdit;
    @BindView(R.id.contextual_edit_marker_snippet)
    EditText snippetEdit;
    @BindView(R.id.contextual_menu_spotType_spinner)
    AppCompatSpinner typeSpinner;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(
                Objects.requireNonNull(getActivity()),
                R.style.CustomDialog_TransparentBackground);

        builder.setView(R.layout.contextual_menu);

        builder.setNegativeButton(getText(R.string.done), (dialog, which) -> sendDialogResponse());

        return builder.create();
    }

    @Override
    public void onStart() {
        super.onStart();
        ButterKnife.bind(this, getDialog());

        setTypeAdapter(getContext());

        Bundle b = getArguments();
        assert b != null;
        String title = b.getString(EXTRA_TITLE, EventMapEditionFragment.defaultTitle);
        String snippet = b.getString(EXTRA_SNIPPET, EventMapEditionFragment.defaultSnippet);
        SpotType type = (SpotType) b.getSerializable(EXTRA_TYPE);
        if (type == null) type = SpotType.ROOM;

        titleEdit.setText(title);
        snippetEdit.setText(snippet);
        typeSpinner.setSelection(type.ordinal());
    }

    private void setTypeAdapter(Context context) {
        ArrayAdapter<SpotType> adapter = new ArrayAdapter<>(context,
                android.R.layout.simple_spinner_item,
                SpotType.values());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        typeSpinner.setAdapter(adapter);

    }

    private void sendDialogResponse() {
        Intent response = Objects.requireNonNull(getActivity()).getIntent();
        response.putExtra(EXTRA_TITLE, getTitle());
        response.putExtra(EXTRA_SNIPPET, getSnippet());
        response.putExtra(EXTRA_TYPE, getType());

        assert getTargetFragment() != null;
        getTargetFragment().onActivityResult(getTargetRequestCode(),
                EventMapEditionFragment.SPOT_INFO_EDITION,
                response);
    }

    private SpotType getType() {
        return (SpotType) typeSpinner.getSelectedItem();
    }

    private String getSnippet() {
        return snippetEdit.getText().toString();
    }

    private String getTitle() {
        return titleEdit.getText().toString();
    }
}
