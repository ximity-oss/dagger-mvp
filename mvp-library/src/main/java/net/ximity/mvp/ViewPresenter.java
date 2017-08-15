package net.ximity.mvp;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;

/**
 * Base view presenter contract with hooks to view lifecycle
 *
 * @author by Emarc Magtanong on 05/07/2016.
 */
public interface ViewPresenter {

    /**
     * Lifecycle callback bound to {@link Activity#onCreate(Bundle)} or {@link Fragment#onCreate(Bundle)}
     */
    void create();

    /**
     * Lifecycle callback for the presenter to start. For a view presenter, this will bind to
     * {@link Activity#onStart()} or {@link Fragment#onStart()}
     */
    void start();

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
