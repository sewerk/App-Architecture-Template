package pl.srw.template.core.di.component;

import pl.srw.template.core.view.activity.MvpActivity;

/**
 * Base Dagger component for activity in {@link pl.srw.template.core.di.scope.RetainActivityScope}
 * @param <T> type of activity using this component
 */
public interface MvpActivityScopeComponent<T extends MvpActivity> {

    /**
     * Inject activity with dependencies
     * @param instance activity
     */
    void inject(T instance);
}
