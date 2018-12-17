package ch.epfl.sweng.eventmanager.ui.event.interaction.fragments.schedule;

import android.app.AlertDialog;
import ch.epfl.sweng.eventmanager.R;
import ch.epfl.sweng.eventmanager.repository.data.ScheduledItem;
import ch.epfl.sweng.eventmanager.ui.event.interaction.MultiFragmentActivity;

public class AdminScheduleFragment extends ScheduleFragment {
    @Override
    protected void onLongPress(ScheduledItem item) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.dialog_delete_scheduled_item)
                .setPositiveButton(R.string.dialog_delete_scheduled_item_confirm, (dialog, id) -> {
                    model.deleteScheduledItem(item);
                })
                .setNegativeButton(R.string.dialog_delete_scheduled_item_cancel, (dialog, id) -> {
                    // User cancelled the dialog
                });
        builder.show();
    }

    @Override
    protected void onShortPress(ScheduledItem item) {
        ((MultiFragmentActivity) requireActivity()).changeFragment(new ScheduleEditFragment(item), true);
    }
}

