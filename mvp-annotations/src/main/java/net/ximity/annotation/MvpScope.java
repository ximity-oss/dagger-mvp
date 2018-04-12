package net.ximity.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Scope;

/**
 * Custom scope for Dagger injected MVP dependencies
 *
 * @author by Emarc Magtanong on 6/16/16.
 */
@Scope
@Retention(RetentionPolicy.RUNTIME)
public @interface MvpScope {
}
