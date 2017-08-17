package net.ximity.sample.home;

import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.util.Log;

import net.ximity.sample.R;

import javax.inject.Inject;

/**
 * Home presenter implementation
 *
 * @author by Emarc Magtanong on 2017/08/17.
 */
public final class HomePresenter implements HomeContract.Presenter {

    /** Log tag **/
    private static final String TAG = "Ximity.Home";
    /** {@link HomeActivity} **/
    private final HomeContract.View view;
    /** Application resources **/
    private final Resources resources;

    @Inject
    HomePresenter(@NonNull HomeContract.View view, @NonNull Resources resources) {
        this.view = view;
        this.resources = resources;
    }

    @Override
    public void logout() {
        view.showError(resources.getString(R.string.logout));
        view.showLogin();
    }

    @Override
    public void create() {
        Log.d(TAG, "Presenter instance: " + this.toString());
        Log.d(TAG, "create()");
    }

    @Override
    public void start() {
        Log.d(TAG, "start()");
    }

    @Override
    public void stop() {
        Log.d(TAG, "stop()");
    }

    @Override
    public void destroy() {
        Log.d(TAG, "destroy()");
    }
}
