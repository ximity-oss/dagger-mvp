package net.ximity.mvp;

/**
 * Scoped component view contract
 *
 * @author by Emarc Magtanong on 2017/04/28.
 */
public interface ViewComponent<V extends MvpView> {
    /**
     * Binds the {@link ViewPresenter} to the scoped activity view lifecycle
     *
     * @param view scoped activity view to bind
     */
    void bind(ActivityView view);

    /**
     * Binds the {@link ViewPresenter} to the scoped fragment view lifecycle
     *
     * @param view scoped fragment view to bind
     */
    void bind(FragmentView view);

    /**
     * Binds the {@link ViewPresenter} to the scoped dialog fragment view lifecycle
     *
     * @param view scoped dialog fragment view to bind
     */
    void bind(DialogView view);

    /**
     * Binds the view dependencies
     *
     * @param view view to bind
     */
    void bind(V view);
}
