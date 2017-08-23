package net.ximity.sample.home.mvp;

import net.ximity.annotation.MvpContract;
import net.ximity.mvp.contract.AuthView;
import net.ximity.mvp.contract.ViewPresenter;
import net.ximity.sample.home.HomeActivity;

/**
 * Home view contract
 *
 * @author by Emarc Magtanong on 2017/08/17.
 */
@MvpContract(view = HomeActivity.class, presenter = HomePresenter.class)
public interface HomeContract {
    interface View extends AuthView {
    }

    interface Presenter extends ViewPresenter {
        /**
         * Fake log out
         */
        void logout();
    }
}
