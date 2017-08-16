package net.ximity.mvp.contract;

/**
 * Authenticated view contract for all view the requires an authenticated user for handling authentication errors
 *
 * @author by Emarc Magtanong on 9/24/16.
 */
public interface AuthView extends MvpView {
    /**
     * Shows the login screen to request for re-authentication
     */
    void showLogin();
}
