package pl.srw.mfvp.presenter;

import pl.srw.mfvp.view.delegate.ViewStateListener;

/**
 * Delegates view state events to associated presenter
 */
public abstract class PresenterHandlingDelegate implements ViewStateListener {

    protected final PresenterOwner view;
    protected boolean isViewBind;

    PresenterHandlingDelegate(PresenterOwner view) {
        this.view = view;
    }

    public boolean isViewBind() {
        return isViewBind;
    }
}
