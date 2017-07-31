package pl.srw.mfvp.view.delegate;

/**
 * Component lifecycle listener
 */
public interface ViewStateListener {

    /**
     * Reacts on view ready
     */
    void onReady();

    /**
     * Reacts on view unavailable
     */
    void onUnavailable();

    /**
     * Reacts on view 'finishing'.
     * View will no longer be available in current scope.
     * This means cleanup can be done here, if necessary.
     */
    void onFinish();
}
