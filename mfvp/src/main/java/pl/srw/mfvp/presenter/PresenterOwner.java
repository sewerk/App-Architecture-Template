package pl.srw.mfvp.presenter;

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
