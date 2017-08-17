package net.ximity.sample;


import net.ximity.mvp.template.BaseApplication;

/**
 * Sample application
 *
 * @author by Emarc Magtanong on 2017/08/16.
 */
public final class SampleApplication extends BaseApplication {

    @Override
    protected SampleComponent initializeMainComponent() {
        return DaggerSampleComponent.builder()
                .sampleModule(new SampleModule(this))
                .build();
    }
}
