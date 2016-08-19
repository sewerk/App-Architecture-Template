package pl.srw.mfvp.view.fragment;

import pl.srw.mfvp.di.component.MvpActivityScopeComponent;
import pl.srw.mfvp.di.component.MvpFragmentScopeComponent;

/**
 * Fragment depending on {@link MvpFragmentScopeComponent}
 * @param <C>   type of associated component
 * @param <AC>  type of associated activity component
 */
public interface MvpFragmentScopedFragment<C extends MvpFragmentScopeComponent, AC extends MvpActivityScopeComponent> {

    /**
     * Gets component instance for this fragment from parent activity component
     * @param activityComponent parent activity component, it should be used to get result fragment component
     * @return component
     */
    C getFragmentComponent(AC activityComponent);
}
