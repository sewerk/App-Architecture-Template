package pl.srw.mfvp.di;

import pl.srw.mfvp.MvpActivity;
import pl.srw.mfvp.MvpFragment;

/**
 * Fragment depending on own {@link MvpComponent}
 * @param <C>   type of associated component
 * @param <AC>  type of associated activity component
 */
public interface MvpFragmentScopedFragment<C extends MvpComponent<? extends MvpFragment>, AC extends MvpComponent<? extends MvpActivity>> {

    /**
     * Prepares component instance for this fragment from parent activity component
     * @param activityComponent parent activity component, it should be used to get result fragment component for proper scoping
     * @return component
     */
    C prepareComponent(AC activityComponent);
}
