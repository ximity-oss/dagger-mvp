package net.ximity.sample;

import net.ximity.annotation.MvpMainComponent;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Sample main component
 *
 * @author by Emarc Magtanong on 2017/08/16.
 */
@MvpMainComponent
@Singleton
@Component(modules = {
        MainModule.class,
})
public abstract class AppComponent implements MvpBindings {
}
