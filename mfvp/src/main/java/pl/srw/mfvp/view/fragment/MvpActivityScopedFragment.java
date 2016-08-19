package pl.srw.mfvp.view.fragment;

import pl.srw.mfvp.di.component.MvpActivityScopeComponent;

/**
 * Fragment depending on {@link MvpActivityScopeComponent}
 * Note: injection must happen on client code due to Dagger limitations
 */
public interface MvpActivityScopedFragment<AC extends MvpActivityScopeComponent> {

    /**
     * Does injection on associated component
     * @param activityComponent component for injection
     */
    void injectDependencies(AC activityComponent);
}
