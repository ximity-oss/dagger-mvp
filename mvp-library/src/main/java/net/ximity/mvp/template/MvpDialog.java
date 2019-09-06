package net.ximity.mvp.template;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

/**
 * Base dialog fragment with dependency injection from the Global Object Graph
 *
 * @author by Emarc Magtanong on 2016/11/18.
 */
public abstract class MvpDialog<M> extends DialogFragment {

    @Override
    @SuppressWarnings("unchecked")
    public void onAttach(Context context) {
        super.onAttach(context);
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
