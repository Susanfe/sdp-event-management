package ch.epfl.sweng.eventmanager.ui.eventShowcase.fragments.schedule;

import android.arch.lifecycle.LiveData;
import ch.epfl.sweng.eventmanager.repository.data.ScheduledItem;

import java.util.List;

public class MyScheduleFragment extends AbstractScheduleFragment {
    @Override
    protected LiveData<List<ScheduledItem>> getScheduledItems() {
        return this.model.getAddedConcerts();
    }
}
