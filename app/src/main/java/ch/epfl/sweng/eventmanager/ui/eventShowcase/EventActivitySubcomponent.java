package ch.epfl.sweng.eventmanager.ui.eventShowcase;

import dagger.Subcomponent;
import dagger.android.AndroidInjector;

import javax.inject.Singleton;

@Subcomponent
@Singleton
public interface EventActivitySubcomponent extends AndroidInjector<EventActivity> {
    @Subcomponent.Builder
    public abstract class Builder extends AndroidInjector.Builder<EventActivity> {
    }
}