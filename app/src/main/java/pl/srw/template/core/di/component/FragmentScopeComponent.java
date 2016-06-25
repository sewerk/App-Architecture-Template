package pl.srw.template.core.di.component;

import pl.srw.template.core.view.fragment.OwnScopeFragment;

/**
 * Base Dagger component for fragment living in {@link pl.srw.template.core.di.scope.RetainFragmentScope}
 * @param <T>    type of fragment using this component
 */
public interface FragmentScopeComponent<T extends OwnScopeFragment> {

    /**
     * Injects fragment dependencies
     * @param instance    fragment
     */
    void inject(T instance);
}
