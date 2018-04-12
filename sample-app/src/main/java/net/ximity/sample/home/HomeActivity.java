package net.ximity.sample.home;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import net.ximity.mvp.template.ActivityView;
import net.ximity.sample.AppComponent;
import net.ximity.sample.R;
import net.ximity.sample.home.mvp.HomeContract;
import net.ximity.sample.home.mvp.HomeContractModule;

import javax.inject.Inject;

public final class HomeActivity extends ActivityView
        implements HomeContract.View {

    @Inject HomeContract.Presenter presenter;

    @Override
    protected void bind(@NonNull AppComponent component) {
        component.add(new HomeContractModule(this))
                .bind(this)
                .bindPresenter(presenter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("Ximity.HomeActivity", "Presenter instance: " + presenter.toString());
        setContentView(R.layout.activity_home);
        Button logout = findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.logout();
            }
        });
    }

    @Override
    public void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean isViewVisible() {
        return true;
    }

    @Override
    public void showLogin() {
        finish();
    }
}
