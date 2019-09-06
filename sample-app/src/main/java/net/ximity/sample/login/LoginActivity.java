package net.ximity.sample.login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import net.ximity.mvp.template.ActivityView;
import net.ximity.sample.AppComponent;
import net.ximity.sample.R;
import net.ximity.sample.home.HomeActivity;
import net.ximity.sample.login.mvp.LoginContract;
import net.ximity.sample.login.mvp.LoginContractModule;

import javax.inject.Inject;

import androidx.annotation.NonNull;

public final class LoginActivity extends ActivityView
        implements LoginContract.View {

    @Inject LoginContract.Presenter presenter;

    @Override
    protected void bind(@NonNull AppComponent component) {
        component.add(new LoginContractModule(this))
                .bind(this)
                .bindPresenter(presenter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("Ximity.LoginActivity", "Presenter instance: " + presenter.toString());
        setContentView(R.layout.activity_login);
        Button login = findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.login();
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
    public void showHome() {
        startActivity(new Intent(this, HomeActivity.class));
    }
}
