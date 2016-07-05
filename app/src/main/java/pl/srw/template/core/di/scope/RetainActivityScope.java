package pl.srw.template.core.di.scope;

import javax.inject.Scope;

/**
 * Dagger scope for single activity type.
 * Dependencies are retain over configuration change but not over activity finish.
 * See {@link pl.srw.template.core.DependencyComponentManager} for scope management
 */
@Scope
public @interface RetainActivityScope {
}
