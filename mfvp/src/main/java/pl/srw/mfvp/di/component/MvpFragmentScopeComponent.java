package pl.srw.mfvp.di.component;

import pl.srw.mfvp.view.fragment.MvpFragmentScopedFragment;

/**
 * Base Dagger component for fragment living in {@link pl.srw.mfvp.di.scope.RetainFragmentScope}
 * @param <T>    type of fragment using this component
 */
public interface MvpFragmentScopeComponent<T extends MvpFragmentScopedFragment> {

    /**
     * Injects fragment dependencies
     * @param instance    fragment
     */
    void inject(T instance);
}
