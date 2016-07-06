package pl.srw.mfvp.di.scope;

import javax.inject.Scope;

/**
 * Dagger scope for single fragment type.
 * Dependencies are retain over configuration change but not over fragment finish.
 * See {@link pl.srw.mfvp.DependencyComponentManager} for scope management
 */
@Scope
public @interface RetainFragmentScope {
}
