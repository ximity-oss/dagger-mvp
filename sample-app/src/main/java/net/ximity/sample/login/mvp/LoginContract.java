package net.ximity.sample.login.mvp;

import net.ximity.annotation.MvpContract;
import net.ximity.mvp.contract.MvpPresenter;
import net.ximity.mvp.contract.MvpView;
import net.ximity.sample.login.LoginActivity;

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

    interface Presenter extends MvpPresenter {
        /**
         * Fake login to go to home screen
         */
        void login();
    }
}
