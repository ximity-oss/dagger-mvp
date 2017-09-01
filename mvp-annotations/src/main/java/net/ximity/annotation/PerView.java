package net.ximity.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Scope;

/**
 * Annotation for per MVP view scoped dependencies
 *
 * @author by Emarc Magtanong on 6/16/16.
 */
@Scope
@Retention(RetentionPolicy.RUNTIME)
public @interface PerView {
}
