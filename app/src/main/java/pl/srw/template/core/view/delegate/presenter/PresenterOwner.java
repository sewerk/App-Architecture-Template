package pl.srw.template.core.view.delegate.presenter;

/**
 * Represents component which owns presenter
 */
public interface PresenterOwner {

    /**
     * Inject dagger dependencies, especially presenter
     */
    void injectDependencies();

    /**
     * Creates presenter delegate
     * @return delegate instance
     */
    PresenterHandlingDelegate createPresenterDelegate();
}
