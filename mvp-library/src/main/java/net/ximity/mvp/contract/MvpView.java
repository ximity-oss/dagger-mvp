package net.ximity.mvp.contract;

/**
 * Marker interface for all views using the MVP pattern
 *
 * @author by Emarc Magtanong on 07/07/2016.
 */
public interface MvpView {
    /**
     * Callback for showing an error message
     *
     * @param message error message
     */
    void showError(String message);

    /**
     * Checks if the view is currently visible to the user
     *
     * @return true if the view is visible to the user, false otherwise
     */
    boolean isViewVisible();
}
