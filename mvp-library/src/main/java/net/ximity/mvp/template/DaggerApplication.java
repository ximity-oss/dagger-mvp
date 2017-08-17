package net.ximity.mvp.template;

import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;

/**
 * Base application with dependency injection methods
 *
 * @author by Emarc Magtanong on 2/12/16.
 */
abstract class DaggerApplication<M> extends Application {

    /** Application main component **/
    private M mMainComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        mMainComponent = initializeMainComponent();
    }

    /**
     * Initializes dagger component for dependency injection.
     */
    protected abstract M initializeMainComponent();

    @SuppressWarnings("unchecked")
    public static <APP extends DaggerApplication> APP get(@NonNull Context context) {
        return (APP) context.getApplicationContext();
    }

    /**
     * Return the main application component for dependency injection
     *
     * @return application's main component
     */
    public M getAppComponent() {
        return mMainComponent;
    }
}
