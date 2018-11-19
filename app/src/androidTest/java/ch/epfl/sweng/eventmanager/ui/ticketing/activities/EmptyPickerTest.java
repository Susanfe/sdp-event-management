package ch.epfl.sweng.eventmanager.ui.ticketing.activities;

import androidx.test.espresso.intent.Intents;
import androidx.recyclerview.widget.RecyclerView;
import ch.epfl.sweng.eventmanager.R;
import ch.epfl.sweng.eventmanager.test.ticketing.MultiHttpStack;
import ch.epfl.sweng.eventmanager.ui.ticketing.TicketingConfigurationPickerActivity;
import ch.epfl.sweng.eventmanager.ui.ticketing.TicketingTestRule;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

public class EmptyPickerTest extends ActivityTest<TicketingConfigurationPickerActivity> {


    public EmptyPickerTest() {
        super(2, TicketingConfigurationPickerActivity.class);
    }

    @Override
    protected TicketingTestRule<TicketingConfigurationPickerActivity> prepareRule(Class<TicketingConfigurationPickerActivity> testClass) {
        manager.setStack(new MultiHttpStack());
        manager.resetService(eventId);

        return super.prepareRule(testClass);
    }

    @After
    public void after() {
        manager.setSetAutomatically(true);
        manager.resetService(eventId);
    }

    @Test
    public void testDisplaysConfigs() {

        RecyclerView recyclerView = mActivityRule.getActivity().findViewById(R.id.recylcer);

        Assert.assertEquals(0, recyclerView.getAdapter().getItemCount());

        Intents.assertNoUnverifiedIntents();
    }
}