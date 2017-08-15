package net.ximity.mvp.annotations;

import net.ximity.mvp.MvpView;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation for MVP contracts
 *
 * @author by Emarc Magtanong on 2017/06/17.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.CLASS)
public @interface Mvp {

    Class<? extends MvpView> view();

    Class<?> presenter();
}
