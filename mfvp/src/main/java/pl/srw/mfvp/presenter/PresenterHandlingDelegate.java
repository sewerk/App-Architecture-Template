package pl.srw.mfvp.presenter;

import pl.srw.mfvp.view.delegate.LifeCycleListener;

/**
 * Delegates lifecycle events to associated presenter
 */
public abstract class PresenterHandlingDelegate implements LifeCycleListener {

    protected final PresenterOwner view;

    PresenterHandlingDelegate(PresenterOwner view) {
        this.view = view;
    }
}
