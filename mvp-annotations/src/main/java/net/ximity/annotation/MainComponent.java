package net.ximity.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Main component marker annotation
 *
 * @author by Emarc Magtanong on 2017/08/16.
 */
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.TYPE)
public @interface MainComponent {
    /**
     * Optional generated MVP component bind interface name.
     *
     * @return generated component binding name
     */
    String value() default "MvpBindings";
}
