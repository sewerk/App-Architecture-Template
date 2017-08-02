package pl.srw.mfvp.presenter;

/**
 * Delegates view state events to associated presenter
 * @param <V> view type
 */
public class PresenterHandlingDelegate<V> {

    protected final V view;
    private final MvpPresenter<V>[] presenters;

    private boolean isViewBind;

    public PresenterHandlingDelegate(V view) {
        this(view, new MvpPresenter[0]);
    }

    public PresenterHandlingDelegate(V view, MvpPresenter<V>... presenter) {
        this.view = view;
        this.presenters = presenter;
    }

    public void onReady() {
        for (int i = 0; i < presenters.length; i++) {
            presenters[i].bind(view);
        }
        isViewBind = true;
    }

    public void onUnavailable() {
        for (int i = 0; i < presenters.length; i++) {
            presenters[i].unbind(view);
        }
        isViewBind = false;
    }

    public void onFinish() {
        for (int i = 0; i < presenters.length; i++) {
            presenters[i].onFinish();
        }
    }

    public boolean isViewBind() {
        return isViewBind;
    }
}
