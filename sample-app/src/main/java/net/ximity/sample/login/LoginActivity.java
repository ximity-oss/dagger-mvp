package net.ximity.sample.login;

import android.os.Bundle;

import net.ximity.mvp.template.ActivityView;
import net.ximity.sample.R;
import net.ximity.sample.SampleComponent;

import javax.inject.Inject;

public final class LoginActivity extends ActivityView
       implements LoginContract.View {

    @Inject LoginContract.Presenter mPresenter;

    @Override
    protected void bind(SampleComponent component) {
        component.add(new LoginContractModule(this))
                .bind(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }
}
