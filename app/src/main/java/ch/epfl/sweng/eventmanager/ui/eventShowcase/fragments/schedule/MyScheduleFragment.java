package ch.epfl.sweng.eventmanager.ui.eventShowcase.fragments.schedule;

import android.arch.lifecycle.LiveData;
import ch.epfl.sweng.eventmanager.R;
import ch.epfl.sweng.eventmanager.repository.data.ScheduledItem;

import java.util.List;

public class MyScheduleFragment extends AbstractScheduleFragment {
    @Override
    protected void setNullConcertsTV() {
        super.nullConcertsTV.setText(R.string.my_schedule_empty);
    }

    @Override
    protected LiveData<List<ScheduledItem>> getScheduledItems() {
        return this.model.getJoinedScheduleItems();
    }
}
