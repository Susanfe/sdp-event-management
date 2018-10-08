package ch.epfl.sweng.eventmanager.ui.eventSelector;

import dagger.Subcomponent;
import dagger.android.AndroidInjector;

import javax.inject.Singleton;

@Subcomponent
public interface EventPickingActivitySubcomponent extends AndroidInjector<EventPickingActivity> {
  @Subcomponent.Builder
  public abstract class Builder extends AndroidInjector.Builder<EventPickingActivity> {}
}