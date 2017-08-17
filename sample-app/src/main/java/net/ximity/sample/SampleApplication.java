package net.ximity.sample;


import net.ximity.mvp.template.BaseApplication;

/**
 * Sample application
 *
 * @author by Emarc Magtanong on 2017/08/16.
 */
public final class SampleApplication extends BaseApplication {

    @Override
    protected AppComponent initializeMainComponent() {
        return DaggerAppComponent.builder()
                .mainModule(new MainModule(this))
                .build();
    }
}
