package ch.epfl.sweng.eventmanager.ui.eventShowcase.fragments.schedule;

import android.arch.lifecycle.LiveData;
import ch.epfl.sweng.eventmanager.R;
import ch.epfl.sweng.eventmanager.data.ScheduledItem;

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

