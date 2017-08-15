package net.ximity.sample;

import net.ximity.mvp.annotations.Mvp;

/**
 * @author by Emarc Magtanong on 2017/06/17.
 */
@Mvp(
        view = MainView.class,
        presenter = MainPresenter.class
)
public interface MainViewContract {
    interface View {
    }

    interface Presenter {
    }
}
