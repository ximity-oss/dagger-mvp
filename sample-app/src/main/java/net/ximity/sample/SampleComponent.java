package net.ximity.sample;

import net.ximity.annotation.MainComponent;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Sample main component
 *
 * @author by Emarc Magtanong on 2017/08/16.
 */
@MainComponent
@Singleton
@Component(modules = {
        SampleModule.class,
})
public interface SampleComponent extends MvpBindings {
}
