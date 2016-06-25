package pl.srw.template.core.view.fragment;

import pl.srw.template.core.di.component.MvpFragmentScopeComponent;

/**
 * Fragment depending on {@link MvpFragmentScopeComponent}
 * @param <C> type of associated component
 */
public interface MvpFragmentScopedFragment<C extends MvpFragmentScopeComponent> {

    /**
     * Prepares new component instance for this fragment
     * @return component
     */
    C prepareComponent();
}
