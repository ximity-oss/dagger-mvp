package net.ximity.mvp.view;

import android.os.Bundle;
import android.support.annotation.Nullable;

import net.ximity.mvp.contract.ViewPresenter;
import net.ximity.mvp.dagger.DaggerActivity;

import javax.inject.Inject;

/**
 * Base activity view with MVP injections and lifecycle bindings
 *
 * @author by Emarc Magtanong on 2017/04/28.
 */
public abstract class BaseActivityView<C> extends DaggerActivity<C> {

    /** Base view presenter **/
    @Inject ViewPresenter mViewPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewPresenter.create();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mViewPresenter.start();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mViewPresenter.stop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mViewPresenter.destroy();
    }
}
