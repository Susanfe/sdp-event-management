package ch.epfl.sweng.eventmanager.ui.ticketing;

import android.content.Intent;
import android.support.test.espresso.intent.Intents;
import ch.epfl.sweng.eventmanager.repository.data.EventTicketingConfiguration;
import ch.epfl.sweng.eventmanager.test.EventTestRule;
import ch.epfl.sweng.eventmanager.test.ticketing.MockStacks;

/**
 * @author Louis Vialar
 */
public class TicketingTestRule<T extends TicketingActivity> extends EventTestRule<T> {
    private EventTicketingConfiguration configuration;

    public TicketingTestRule(Class<T> activityClass) {
        super(activityClass);
        configuration = MockStacks.BASIC_CONFIGURATION;
    }

    public TicketingTestRule(Class<T> activityClass, int eventId, EventTicketingConfiguration configuration) {
        super(activityClass, eventId);
        this.configuration = configuration;
    }

    @Override
    protected Intent getActivityIntent() {
        Intent intent = super.getActivityIntent();
        intent.putExtra(TicketingActivity.TICKETING_CONFIGURATION, configuration);
        return intent;
    }
}
