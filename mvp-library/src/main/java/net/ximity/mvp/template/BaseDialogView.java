package net.ximity.mvp.template;

import android.os.Bundle;
import android.support.annotation.Nullable;

import net.ximity.mvp.contract.ViewPresenter;

import javax.inject.Inject;

/**
 * Base dialog fragment view with MVP injections and lifecycle bindings
 *
 * @author by Emarc Magtanong on 2017/04/28.
 */
abstract class BaseDialogView<C> extends DaggerDialog<C> {

    /** Base view presenter **/
    @Inject ViewPresenter mViewPresenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewPresenter.create();
    }

    @Override
    public void onStart() {
        super.onStart();
        mViewPresenter.start();
    }

    @Override
    public void onStop() {
        super.onStop();
        mViewPresenter.stop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mViewPresenter.destroy();
    }
}
