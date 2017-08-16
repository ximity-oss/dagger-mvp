package net.ximity.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * MVP contract annotation
 *
 * @author by Emarc Magtanong on 2017/08/16.
 */
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.TYPE)
public @interface MvpContract {

    /**
     * Required implemented presenter class
     */
    Class<?> presenter();

    /**
     * Required implemented view class
     */
    Class<?> view();

    /**
     * Optional generated module name
     */
    String moduleName() default "";

    /**
     * Optional generated subcomponent name
     */
    String subcomponentName() default "";
}
