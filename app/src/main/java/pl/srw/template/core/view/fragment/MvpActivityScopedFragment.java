package pl.srw.template.core.view.fragment;

import pl.srw.template.core.di.component.MvpActivityScopeComponent;
import pl.srw.template.core.MvpActivity;

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
