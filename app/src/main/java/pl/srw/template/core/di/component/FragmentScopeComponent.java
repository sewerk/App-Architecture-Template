package pl.srw.template.core.di.component;

import pl.srw.template.core.di.OwnScopeFragment;
import pl.srw.template.core.presenter.BasePresenter;

/**
 * Base Dagger component for fragment living in {@link pl.srw.template.core.di.scope.RetainFragmentScope}
 * @param <T>    type of fragment using this component
 * @param <P>    type of presenter used for associated fragment type
 */
public interface FragmentScopeComponent<T extends OwnScopeFragment, P extends BasePresenter> {

    /**
     * Injects fragment dependencies
     * @param instance    fragment
     */
    void inject(T instance);

    /**
     * Gets presenter used by associated fragment
     * @return fragment presenter
     */
    P getPresenter();
}
