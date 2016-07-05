package pl.srw.template.core.di.scope;

import javax.inject.Scope;

/**
 * Dagger scope for single fragment type.
 * Dependencies are retain over configuration change but not over fragment finish.
 * See {@link pl.srw.template.core.DependencyComponentManager} for scope management
 */
@Scope
public @interface RetainFragmentScope {
}
