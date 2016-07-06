package pl.srw.mfvp.di.component;

import pl.srw.mfvp.MvpActivity;

/**
 * Base Dagger component for activity in {@link pl.srw.mfvp.di.scope.RetainActivityScope}
 * @param <T> type of activity using this component
 */
public interface MvpActivityScopeComponent<T extends MvpActivity> {

    /**
     * Inject activity with dependencies
     * @param instance activity
     */
    void inject(T instance);
}
