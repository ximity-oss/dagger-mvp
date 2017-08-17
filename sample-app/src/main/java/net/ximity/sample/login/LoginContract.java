package net.ximity.sample.login;

import net.ximity.annotation.MvpContract;
import net.ximity.mvp.contract.AuthView;
import net.ximity.mvp.contract.ViewPresenter;

/**
 * Sample login view contract
 *
 * @author by Emarc Magtanong on 2017/08/17.
 */
@MvpContract(view = LoginActivity.class, presenter = LoginPresenter.class)
public interface LoginContract {
    interface View extends AuthView {
    }

    interface Presenter extends ViewPresenter {
    }
}
