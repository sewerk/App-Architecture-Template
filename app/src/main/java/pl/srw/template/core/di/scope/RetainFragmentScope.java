package pl.srw.template.core.di.scope;

import javax.inject.Scope;

import pl.srw.template.core.di.DependencyComponentManager;

/**
 * Dagger scope for single fragment type.
 * Dependencies are retain over configuration change but not over fragment finish.
 * See {@link DependencyComponentManager} for scope management
 */
@Scope
public @interface RetainFragmentScope {
}
