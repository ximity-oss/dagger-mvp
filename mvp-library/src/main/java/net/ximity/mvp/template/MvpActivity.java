package net.ximity.mvp.template;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * Base activity with dependency injection from the Global Object Graph
 *
 * @author by Emarc Magtanong on 2/12/16.
 */
public abstract class MvpActivity<M> extends AppCompatActivity {

    @SuppressWarnings("unchecked")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
