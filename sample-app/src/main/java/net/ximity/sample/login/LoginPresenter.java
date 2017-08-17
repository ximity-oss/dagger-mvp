package net.ximity.sample.login;

import android.content.res.Resources;
import android.support.annotation.NonNull;

import javax.inject.Inject;

/**
 * Sample login presenter implementation
 *
 * @author by Emarc Magtanong on 2017/08/17.
 */
public final class LoginPresenter implements LoginContract.Presenter {

    private final Resources resources;

    @Inject
    LoginPresenter(@NonNull Resources resources) {
        this.resources = resources;
    }

    @Override
    public void create() {
    }

    @Override
    public void start() {
    }

    @Override
    public void stop() {
    }

    @Override
    public void destroy() {
    }
}
