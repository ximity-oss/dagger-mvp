package net.ximity.sample.login;

import net.ximity.annotation.MvpContract;
import net.ximity.mvp.contract.MvpView;
import net.ximity.mvp.contract.ViewPresenter;

/**
 * Login view contract
 *
 * @author by Emarc Magtanong on 2017/08/17.
 */
@MvpContract(view = LoginActivity.class, presenter = LoginPresenter.class)
public interface LoginContract {
    interface View extends MvpView {
        /**
         * Shows the home screen
         */
        void showHome();
    }

    interface Presenter extends ViewPresenter {
        /**
         * Fake login to go to home screen
         */
        void login();
    }
}
