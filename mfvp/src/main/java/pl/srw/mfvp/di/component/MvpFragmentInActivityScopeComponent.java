package pl.srw.mfvp.di.component;

import pl.srw.mfvp.view.fragment.MvpActivityScopedFragment;

/**
 * Base Dagger component for fragment living in {@link pl.srw.mfvp.di.scope.RetainActivityScope}
 * @param <F> type of fragment using this component
 */
public interface MvpFragmentInActivityScopeComponent<F extends MvpActivityScopedFragment> {

    /**
     * Injects fragments dependencies
     * @param instance fragment
     */
    void inject(F instance);
}
