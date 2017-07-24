package pl.srw.mfvp.view.delegate;

/**
 * Component lifecycle listener
 */
public interface LifeCycleListener {

    /**
     * Reacts on onStart callback
     */
    void onStart();

    /**
     * Reacts on onStop callback
     */
    void onStop();

    /**
     * Reacts on 'view went out of scope' callback
     */
    void onEnd();
}
