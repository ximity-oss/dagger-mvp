package net.ximity.mvp.template;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

/**
 * Base fragment with dependency injection from the Global Object Graph
 *
 * @author by Emarc Magtanong on 4/4/16.
 */
public abstract class MvpFragment<M> extends Fragment {

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
