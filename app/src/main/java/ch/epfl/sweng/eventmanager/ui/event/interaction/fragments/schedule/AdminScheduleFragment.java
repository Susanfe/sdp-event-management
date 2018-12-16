package ch.epfl.sweng.eventmanager.ui.event.interaction.fragments.schedule;

import ch.epfl.sweng.eventmanager.repository.data.ScheduledItem;

public class AdminScheduleFragment extends ScheduleFragment {
    @Override
    protected void onLongPress(ScheduledItem item) {
        // TODO: confirm deletion
    }

    @Override
    protected void onShortPress(ScheduledItem item) {
        // TODO: edit item
    }
}

