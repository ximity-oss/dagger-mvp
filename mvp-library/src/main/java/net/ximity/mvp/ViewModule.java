package net.ximity.mvp;

/**
 * View module contract to provide view module with {@link ViewPresenter}
 *
 * @author by Emarc Magtanong on 2017/04/28.
 */
public interface ViewModule<P extends ViewPresenter> {
    /**
     * Provides the implementation of a {@link ViewPresenter}.
     *
     * @param impl {@link ViewPresenter} implementation
     * @return a {@link ViewPresenter}
     */
    ViewPresenter providesBaseViewPresenter(P impl);
}
