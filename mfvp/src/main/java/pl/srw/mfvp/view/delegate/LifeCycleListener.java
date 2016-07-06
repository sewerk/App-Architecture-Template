package pl.srw.mfvp.view.delegate;

/**
 * Base class for additional functionality based on component lifecycle
 */
public abstract class LifeCycleListener {

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
     * Reacts on 'view went out of scope' callback
     */
    public void onEnd() {
    }
}
