package pl.srw.mfvp;

/**
 * Delegates view state events to associated presenter
 * @param <V> view type
 */
public class PresenterHandlingDelegate<V> {

    protected final V view;
    private final MvpPresenter<V>[] presenters;

    private boolean isViewBound;

    PresenterHandlingDelegate(V view) {
        this(view, new MvpPresenter[0]);
    }

    PresenterHandlingDelegate(V view, MvpPresenter<V>... presenter) {
        this.view = view;
        this.presenters = presenter;
    }

    void onReady() {
        for (int i = 0; i < presenters.length; i++) {
            presenters[i].bind(view);
        }
        isViewBound = true;
    }

    void onUnavailable() {
        for (int i = 0; i < presenters.length; i++) {
            presenters[i].unbind(view);
        }
        isViewBound = false;
    }

    void onFinish() {
        for (int i = 0; i < presenters.length; i++) {
            presenters[i].onFinish();
        }
    }

    boolean isViewBound() {
        return isViewBound;
    }
}
