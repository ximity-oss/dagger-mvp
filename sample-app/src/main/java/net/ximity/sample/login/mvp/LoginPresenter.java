package net.ximity.sample.login.mvp;

import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;

import net.ximity.annotation.MvpScope;
import net.ximity.sample.R;
import net.ximity.sample.login.LoginActivity;

import javax.inject.Inject;

import androidx.annotation.NonNull;

/**
 * Sample login presenter implementation
 *
 * @author by Emarc Magtanong on 2017/08/17.
 */
@MvpScope
public final class LoginPresenter implements LoginContract.Presenter {

    /** Log tag **/
    private static final String TAG = "Ximity.Login";
    /** {@link LoginActivity} **/
    private final LoginContract.View view;
    /** Application resources **/
    private final Resources resources;

    @Inject
    LoginPresenter(@NonNull LoginContract.View view, @NonNull Resources resources) {
        this.view = view;
        this.resources = resources;
    }

    @Override
    public void login() {
        view.showError(resources.getString(R.string.login));
        view.showHome();
    }

    @Override
    public void create(@NonNull Bundle saved) {
        Log.d(TAG, "Presenter instance: " + this.toString());
        Log.d(TAG, "create()");
    }

    @Override
    public void start() {
        Log.d(TAG, "start()");
    }

    @Override
    public void pause() {
        Log.d(TAG, "pause()");
    }

    @Override
    public void saveState(@NonNull Bundle out) {
        Log.d(TAG, "saveState()");
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
