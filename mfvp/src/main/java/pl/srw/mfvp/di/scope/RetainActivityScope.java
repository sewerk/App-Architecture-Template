package pl.srw.mfvp.di.scope;

import javax.inject.Scope;

/**
 * Dependency scope for activities and fragments in activity scope.
 * Dependencies are retain over configuration change but not over activity finish.
 * See {@link pl.srw.mfvp.DependencyComponentManager} for scope management
 */
@Scope
public @interface RetainActivityScope {
}
