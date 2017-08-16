package net.ximity.mvp.dagger;

import android.app.Service;

/**
 * Base Service with dependency injection from the Global Object Graph
 *
 * @author by Emarc Magtanong on 4/4/16.
 */
public abstract class DaggerService<M> extends Service {

    @Override
    @SuppressWarnings("unchecked")
    public void onCreate() {
        super.onCreate();
        M component = (M) DaggerApplication.get(this).getAppComponent();
        bind(component);
    }

    /**
     * Injects dependencies with global scope
     *
     * @param mainComponent component to bind globally scoped dependencies
     */
    protected abstract void bind(M mainComponent);
}
