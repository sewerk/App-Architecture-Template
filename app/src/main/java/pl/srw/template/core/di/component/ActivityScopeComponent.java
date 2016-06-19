package pl.srw.template.core.di.component;

import java.util.Set;

import pl.srw.template.core.presenter.BasePresenter;
import pl.srw.template.core.view.BaseActivity;

/**
 * Base Dagger component for {@link pl.srw.template.core.di.scope.RetainActivityScope}
 * @param <T> type of activity using this component
 */
public interface ActivityScopeComponent<T extends BaseActivity> {

    /**
     * Inject activity with dependencies
     * @param instance activity
     */
    void inject(T instance);

    /**
     * Gets all presenters in scope of this component
     * @return presenters
     */
    Set<BasePresenter> getPresenters();
}
