package ch.epfl.sweng.eventmanager.ui.CustomViews;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ch.epfl.sweng.eventmanager.R;
import ch.epfl.sweng.eventmanager.ui.event.interaction.fragments.EventMapEditionFragment;

public class CustomAddOptionsDialog extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(
                Objects.requireNonNull(getActivity()),
                R.style.CustomDialog_TransparentBackground);

        builder.setView(R.layout.contextual_edition_add);

        return builder.create();
    }

    @Override
    public void onStart() {
        super.onStart();
        ButterKnife.bind(this, getDialog());
    }

    @OnClick({R.id.contextual_add_spot_button,
                R.id.contextual_add_overlay_button})
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.contextual_add_spot_button:
                sendDialogResponse(EventMapEditionFragment.ADD_SPOT);
                dismiss();
                break;

            case R.id.contextual_add_overlay_button:
                sendDialogResponse(EventMapEditionFragment.ADD_OVERLAY_EDGE);
                dismiss();
                break;
        }
    }

    private void sendDialogResponse(int requestCode) {
        assert getTargetFragment() != null;
        getTargetFragment().onActivityResult(getTargetRequestCode(),
                requestCode,
                Objects.requireNonNull(getActivity()).getIntent());
    }
}
