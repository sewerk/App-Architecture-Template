package pl.srw.mfvp.presenter;

import android.support.annotation.CallSuper;

import timber.log.Timber;

/**
 * Parent class for all presenters
 *
 * @param <V> view type to present on
 */
public abstract class MvpPresenter<V> {

    private V view;
    private boolean firstBind = true;
    private UIChange<V> latestUIChange;

    public MvpPresenter() {
        Timber.d("New presenter created " + this.getClass().getSimpleName());
    }

    /**
     * Bind view to presenter
     *
     * @param view view to control
     */
    @CallSuper
    public void bind(V view) {
        if (this.view != null) {
            throw new RuntimeException("Concurrent view bind! Unexpected, second instance of view occurred"
                    + " before first one unbind.");
        }
        this.view = view;
        if (firstBind) {
            onFirstBind();
            firstBind = false;
        } else {
            onNewViewRestoreState();
        }
        if (latestUIChange != null) {
            present(latestUIChange);
        }
    }

    /**
     * Does additional work only on first bind view
     */
    protected void onFirstBind() {
    }

    /**
     * Does additional work on second and every next bind
     */
    protected void onNewViewRestoreState() {
    }

    /**
     * Make changes on view, if view is currently available. If not changes are postponed until next bind.
     *
     * @param uiChange change view action
     */
    protected final void present(UIChange<V> uiChange) {
        if (view != null) {
            uiChange.change(view);
            latestUIChange = null;
        } else {
            latestUIChange = uiChange;
        }
    }

    /**
     * Unbind view from presenter
     */
    @CallSuper
    public void unbind() {
        view = null;
    }

    /**
     * Cleanup after view is being destroyed
     */
    @CallSuper
    public void onFinish() {
        Timber.d("Presenter finishing " + this.getClass().getSimpleName());
    }

    /**
     * Wrapper for making changes on view, when the view is bind
     *
     * @param <V> view type
     */
    public interface UIChange<V> {

        /**
         * Execute change on view
         *
         * @param view view to change
         */
        void change(V view);
    }
}