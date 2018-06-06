package net.ximity.mvp.template;

import android.app.Service;
import android.support.annotation.NonNull;

/**
 * Base Service with dependency injection from the Global Object Graph
 *
 * @author by Emarc Magtanong on 4/4/16.
 */
public abstract class MvpService<M> extends Service {

    @Override
    @SuppressWarnings("unchecked")
    public void onCreate() {
        super.onCreate();
        M component = (M) MvpApplication.getApp(this).getComponent();
        bind(component);
    }

    /**
     * Injects dependencies with global scope
     *
     * @param component component to bind globally scoped dependencies
     */
    protected abstract void bind(@NonNull M component);
}
