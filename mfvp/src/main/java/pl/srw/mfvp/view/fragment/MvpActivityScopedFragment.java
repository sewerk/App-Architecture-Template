package pl.srw.mfvp.view.fragment;

import pl.srw.mfvp.di.component.MvpActivityScopeComponent;
import pl.srw.mfvp.MvpActivity;

/**
 * Fragment depending on {@link MvpActivityScopeComponent}
 */
public interface MvpActivityScopedFragment {

    /**
     * Gets hosted activity
     * @return activity
     */
    MvpActivity getBaseActivity();
}
