package pl.srw.mfvp.view.delegate.presenter;

import pl.srw.mfvp.view.delegate.LifeCycleListener;

/**
 * Base class for delegate managing associated presenter with its owner lifecycle
 */
public abstract class PresenterHandlingDelegate extends LifeCycleListener {

    protected final PresenterOwner view;

    public PresenterHandlingDelegate(PresenterOwner view) {
        this.view = view;
    }
}
