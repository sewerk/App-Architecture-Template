package pl.srw.template.core.di.component;

import pl.srw.template.core.view.fragment.ActivityScopedFragment;

/**
 * Base Dagger component for fragment living in {@link pl.srw.template.core.di.scope.RetainActivityScope}
 * @param <F> type of fragment using this component
 */
public interface FragmentInActivityScopeComponent<F extends ActivityScopedFragment> {

    /**
     * Injects fragments dependencies
     * @param instance fragment
     */
    void inject(F instance);
}
