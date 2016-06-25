package pl.srw.template.core.di.component;

import pl.srw.template.core.view.fragment.MvpFragmentScopedFragment;

/**
 * Base Dagger component for fragment living in {@link pl.srw.template.core.di.scope.RetainFragmentScope}
 * @param <T>    type of fragment using this component
 */
public interface MvpFragmentScopeComponent<T extends MvpFragmentScopedFragment> {

    /**
     * Injects fragment dependencies
     * @param instance    fragment
     */
    void inject(T instance);
}
