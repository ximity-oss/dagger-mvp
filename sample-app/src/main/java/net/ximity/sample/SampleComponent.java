package net.ximity.sample;

import net.ximity.annotation.MvpComponent;

import dagger.Component;

/**
 * Sample main component
 *
 * @author by Emarc Magtanong on 2017/08/16.
 */
@MvpComponent
@Component(modules = {
        SampleModule.class,
})
public interface SampleComponent extends MvpBindings {
}
