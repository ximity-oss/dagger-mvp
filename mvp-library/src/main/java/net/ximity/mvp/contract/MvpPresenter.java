package net.ximity.mvp.contract;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

/**
 * Base view presenter contract with hooks to view lifecycle
 *
 * @author by Emarc Magtanong on 05/07/2016.
 */
public interface MvpPresenter {

    /**
     * Lifecycle callback bound to {@link Activity#onCreate(Bundle)} or
     * {@link Fragment#onViewCreated(View, Bundle)}
     *
     * @param saved saved state bundle, non-null if view was previously recreated
     */
    void create(@Nullable Bundle saved);

    /**
     * Lifecycle callback for the presenter to start. For a view presenter, this will bind to
     * {@link Activity#onStart()} or {@link Fragment#onStart()}
     */
    void start();

    /**
     * Lifecycle callback for the presenter to pause. For a view presenter, this will bind to
     * {@link Activity#onPause()} or {@link Fragment#onPause()}
     */
    void pause();

    /**
     * Lifecycle callback for the presenter to save state. For a view presenter, this will bind to
     * {@link Activity#onSaveInstanceState(Bundle)} or {@link Fragment#onSaveInstanceState(Bundle)}
     *
     * @param out out state bundle
     */
    void saveState(@NonNull Bundle out);

    /**
     * Lifecycle callback for the presenter to stop. For a view presenter, this will bind to
     * {@link Activity#onStop()} or {@link Fragment#onStop()}
     */
    void stop();

    /**
     * Lifecycle callback bound to {@link Activity#onDestroy()} or {@link Fragment#onDestroy()}
     */
    void destroy();
}
