package pl.srw.template.core.view.fragment;

import pl.srw.template.core.di.component.FragmentScopeComponent;

/**
 * Fragment depending on {@link FragmentScopeComponent}
 * @param <C> type of associated component
 */
public interface OwnScopeFragment<C extends FragmentScopeComponent> {

    /**
     * Prepares new component instance for this fragment
     * @return component
     */
    C prepareComponent();
}
