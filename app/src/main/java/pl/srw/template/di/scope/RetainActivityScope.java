package pl.srw.template.di.scope;

import javax.inject.Scope;

import pl.srw.template.di.DependencyComponentManager;

/**
 * Dagger scope for single activity type.
 * Dependencies are retain over configuration change but not over activity finish.
 * See {@link DependencyComponentManager} for scope management
 */
@Scope
public @interface RetainActivityScope {
}
