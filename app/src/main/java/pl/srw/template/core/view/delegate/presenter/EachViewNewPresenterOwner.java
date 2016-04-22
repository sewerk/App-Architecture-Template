package pl.srw.template.core.view.delegate.presenter;

/**
 * Interface for views which needs new presenter on each view entry
 */
public interface EachViewNewPresenterOwner extends PresenterOwner {

    /**
     * Release dependency graph
     */
    void resetDependencies();
}
