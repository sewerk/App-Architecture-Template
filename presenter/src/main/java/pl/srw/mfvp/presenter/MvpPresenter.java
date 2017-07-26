package pl.srw.mfvp.presenter;

import android.support.annotation.CallSuper;

import java.util.LinkedList;
import java.util.Queue;

import timber.log.Timber;

/**
 * Parent class for all presenters
 *
 * @param <V> view type to present on
 */
public abstract class MvpPresenter<V> {

    private V view;
    private boolean firstBind = true;
    private Queue<UIChange<V>> changes = new LinkedList<>();

    protected MvpPresenter() {
        Timber.v("Creating " + this.getClass().getSimpleName());
    }

    /**
     * Bind view to presenter.
     * If some UI changes were queued during view was missing
     * they will be executed immediately on this new view
     *
     * @param view view to control
     */
    final void bind(V view) {
        this.view = view;
        if (firstBind) {
            onFirstBind();
            firstBind = false;
        } else {
            onNewViewRestoreState();
        }
        UIChange<V> uiChange;
        while ((uiChange = changes.poll()) != null) {
            uiChange.change(view);
        }
    }

    /**
     * Unbind view from presenter
     * If passed view differs from current bind then current will persist,
     * as this is assumed to be late unbind which is no more relevant
     *
     * @param view view to unbind
     */
    final void unbind(V view) {
        if (this.view == view) this.view = null;
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
     * Does additional work when user goes back to the view
     * @param view bind view
     */
    protected void onRestart(V view) {
    }

    /**
     * Does additional work when view starts being visible to the user
     * @param view bind view
     */
    protected void onViewVisible(V view) {
    }

    /**
     * Does additional work when view stops being visible to the user
     * @param view bind view
     */
    protected void onViewHidden(V view) {
    }

    /**
     * Cleanup after view is being destroyed
     */
    @CallSuper
    protected void onFinish() {
        Timber.v("Finishing " + this.getClass().getSimpleName());
    }

    /**
     * Make changes on view, if view is currently available.
     * If not changes are postponed until next bind.
     *
     * @param uiChange change view action
     */
    protected final void present(UIChange<V> uiChange) {
        if (view != null) {
            uiChange.change(view);
        } else {
            changes.add(uiChange);
        }
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