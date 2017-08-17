package net.ximity.sample;

import android.app.Application;
import android.content.res.Resources;
import android.support.annotation.NonNull;

import javax.inject.Singleton;

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
    private final Application mApplication;

    MainModule(@NonNull Application application) {
        mApplication = application;
    }

    @Provides
    @Singleton
    Application providesApplication() {
        return mApplication;
    }

    @Provides
    @Singleton
    Resources providesResources() {
        return mApplication.getResources();
    }
}
