package net.ximity.sample;

import android.app.Application;
import android.content.res.Resources;

import javax.inject.Singleton;

import androidx.annotation.NonNull;
import dagger.Module;
import dagger.Provides;

/**
 * Sample module
 *
 * @author by Emarc Magtanong on 2017/08/16.
 */
@Module
public final class MainModule {

    /** Application **/
    private final Application mt;

    MainModule(@NonNull Application application) {
        mt = application;
    }

    @Provides
    @Singleton
    Application providesApplication() {
        return mt;
    }

    @Provides
    @Singleton
    Resources providesResources() {
        return mt.getResources();
    }
}
