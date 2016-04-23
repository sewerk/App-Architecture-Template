package pl.srw.template.core.view.delegate.presenter;

import pl.srw.template.core.view.delegate.LifeCycleDelegating;

/**
 * Base class for delegate managing associated presenter with its owner lifecycle
 */
public abstract class PresenterHandlingDelegate extends LifeCycleDelegating {

    protected final PresenterOwner view;

    public PresenterHandlingDelegate(PresenterOwner view) {
        this.view = view;
    }
}
