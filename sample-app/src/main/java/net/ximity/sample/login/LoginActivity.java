package net.ximity.sample.login;

import android.os.Bundle;

import net.ximity.sample.ActivityView;
import net.ximity.sample.R;
import net.ximity.sample.SampleComponent;

public final class LoginActivity extends ActivityView
       implements LoginContract.View {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    @Override
    protected void bind(SampleComponent component) {

    }

    @Override
    public void showError(String message) {

    }

    @Override
    public boolean isViewVisible() {
        return false;
    }

    @Override
    public void showLogin() {

    }
}
