package ch.epfl.sweng.eventmanager.ui.ticketing;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * @author Louis Vialar
 */
@Module
public abstract class TicketingModule {
    @ContributesAndroidInjector
    abstract TicketingLoginActivity loginActivity();

    @ContributesAndroidInjector
    abstract TicketingScanActivity scanActivity();

    @ContributesAndroidInjector
    abstract TicketingConfigurationPickerActivity configurationPickerActivity();
}

