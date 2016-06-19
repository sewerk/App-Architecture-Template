package pl.srw.template.core.di;

import pl.srw.template.core.view.BaseActivity;

/**
 * Fragment depending on {@link pl.srw.template.core.di.component.ActivityScopeComponent}
 */
public interface ActivityScopedFragment {

    /**
     * Gets hosted activity
     * @return activity
     */
    BaseActivity getBaseActivity();
}
