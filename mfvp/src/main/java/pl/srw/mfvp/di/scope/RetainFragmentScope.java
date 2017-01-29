package pl.srw.mfvp.di.scope;

import javax.inject.Scope;

/**
 * Dependency scope for fragments in own scope.
 * Dependencies are retain over configuration change but not over fragment finish.
 * See {@link pl.srw.mfvp.DependencyComponentManager} for scope management
 */
@Scope
public @interface RetainFragmentScope {
}
