package pl.srw.mfvp.view.delegate.presenter;

/**
 * Represents component which owns presenter
 */
public interface PresenterOwner {

    /**
     * Creates presenter delegate
     * @return delegate instance
     */
    PresenterHandlingDelegate createPresenterDelegate();
}
