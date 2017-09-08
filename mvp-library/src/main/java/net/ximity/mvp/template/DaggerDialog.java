package net.ximity.mvp.template;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

/**
 * Base dialog fragment with dependency injection from the Global Object Graph
 *
 * @author by Emarc Magtanong on 2016/11/18.
 */
public abstract class DaggerDialog<M> extends DialogFragment {

    @Override
    @SuppressWarnings("unchecked")
    public void onAttach(Context context) {
        super.onAttach(context);
        M component = (M) DaggerApplication.getApp(context).getComponent();
        bind(component);
    }

    /**
     * Injects dependencies with global scope
     *
     * @param mainComponent component to bind globally scoped dependencies
     */
    protected abstract void bind(@NonNull M mainComponent);
}
