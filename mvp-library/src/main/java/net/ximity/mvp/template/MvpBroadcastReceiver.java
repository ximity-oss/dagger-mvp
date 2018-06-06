package net.ximity.mvp.template;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

/**
 * Base broadcast receiver with dependency injection from the Global Object Graph
 *
 * @author by Emarc Magtanong on 4/4/16.
 */
public abstract class MvpBroadcastReceiver<M> extends BroadcastReceiver {

    @Override
    @SuppressWarnings("unchecked")
    public void onReceive(Context context, Intent intent) {
        M component = (M) MvpApplication.getApp(context).getComponent();
        bind(component);
    }

    /**
     * Injects dependencies with global scope
     *
     * @param component component to bind globally scoped dependencies
     */
    protected abstract void bind(@NonNull M component);
}
