package pl.srw.mfvp.view.delegate.presenter;

import pl.srw.mfvp.presenter.MvpPresenter;

/**
 * Delegate for managing multiple presenters according to component lifecycle
 */
public class MultiplePresenterHandlingDelegate extends PresenterHandlingDelegate {

    private final MvpPresenter[] presenters;

    public MultiplePresenterHandlingDelegate(PresenterOwner view, MvpPresenter... presenter) {
        super(view);
        this.presenters = presenter;
    }

    @Override
    public void onStart() {
        for (int i = 0; i < presenters.length; i++) {
            presenters[i].bind(view);
        }
    }

    @Override
    public void onStop() {
        for (int i = 0; i < presenters.length; i++) {
            presenters[i].unbind();
        }
    }

    @Override
    public void onEnd() {
        for (int i = 0; i < presenters.length; i++) {
            presenters[i].onFinish();
        }
    }
}
