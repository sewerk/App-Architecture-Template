package pl.srw.template.core.view.fragment;

import pl.srw.template.core.view.activity.MvpActivity;

/**
 * Fragment depending on {@link pl.srw.template.core.di.component.ActivityScopeComponent}
 */
public interface ActivityScopedFragment {

    /**
     * Gets hosted activity
     * @return activity
     */
    MvpActivity getBaseActivity();
}
