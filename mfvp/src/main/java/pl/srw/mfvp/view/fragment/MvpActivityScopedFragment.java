package pl.srw.mfvp.view.fragment;

import pl.srw.mfvp.di.component.MvpComponent;

/**
 * Fragment depending on activity {@link MvpComponent}
 * Note: injection must happen on client code due to Dagger limitations
 */
public interface MvpActivityScopedFragment<AC extends MvpComponent> {

    /**
     * Does injection on associated component
     * @param activityComponent component for injection
     */
    void injectDependencies(AC activityComponent);
}
