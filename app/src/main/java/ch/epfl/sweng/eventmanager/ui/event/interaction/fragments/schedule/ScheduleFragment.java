package ch.epfl.sweng.eventmanager.ui.event.interaction.fragments.schedule;

import android.arch.lifecycle.LiveData;
import ch.epfl.sweng.eventmanager.R;
import ch.epfl.sweng.eventmanager.repository.data.ScheduledItem;

import java.util.List;

public class ScheduleFragment extends AbstractScheduleFragment {
    @Override
    protected int getLayout() {
        return R.layout.activity_schedule;
    }

    @Override
    protected void setEmptyListTextView() {
        super.emptyListTextView.setText(R.string.scheduled_items_empty);
    }

    @Override
    protected LiveData<List<ScheduledItem>> getScheduledItems() {
        return this.model.getScheduledItems();
    }
}
