package pl.srw.template.core.view.delegate;

/**
 * Base class for additional functionality based on component lifecycle
 */
public abstract class LifeCycleDelegating {

    /**
     * Reacts on onStart callback
     */
    public void onStart() {
    }

    /**
     * Reacts on onStop callback
     */
    public void onStop() {
    }

    /**
     * Reacts on 'view is finishing' callback
     */
    public void onFinish() {
    }
}
