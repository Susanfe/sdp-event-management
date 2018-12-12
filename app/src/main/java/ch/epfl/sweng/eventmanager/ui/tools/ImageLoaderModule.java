package ch.epfl.sweng.eventmanager.ui.tools;

import dagger.Binds;
import dagger.Module;

import javax.inject.Singleton;

/**
 * @author Louis Vialar
 */
@Module
public abstract class ImageLoaderModule {
    @Binds
    @Singleton
    abstract ImageLoader providesNewsRepository(GlideImageLoader impl);
}
