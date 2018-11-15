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
    private int configId = -1; // for Scanning activity

    public TicketingTestRule(Class<T> activityClass) {
        super(activityClass);
        configuration = MockStacks.BASIC_CONFIGURATION;
    }

    public TicketingTestRule(Class<T> activityClass, int eventId, EventTicketingConfiguration configuration) {
        super(activityClass, eventId);
        this.configuration = configuration;
    }

    public TicketingTestRule<T> withConfigId(int configId) {
        this.configId = configId;
        return this;
    }

    @Override
    protected Intent getActivityIntent() {
        Intent intent = super.getActivityIntent();
        intent.putExtra(TicketingActivity.TICKETING_CONFIGURATION, configuration);
        if (configId != -1)
            intent.putExtra(TicketingScanActivity.SELECTED_CONFIG_ID, configId);
        return intent;
    }
}
