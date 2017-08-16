package net.ximity.mvp.dagger;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Base broadcast receiver with dependency injection from the Global Object Graph
 *
 * @author by Emarc Magtanong on 4/4/16.
 */
public abstract class DaggerBroadcastReceiver<M> extends BroadcastReceiver {

    @Override
    @SuppressWarnings("unchecked")
    public void onReceive(Context context, Intent intent) {
        M component = (M) DaggerApplication.get(context).getAppComponent();
        bind(component);
    }

    /**
     * Injects dependencies with global scope
     *
     * @param mainComponent component to bind globally scoped dependencies
     */
    protected abstract void bind(M mainComponent);
}
