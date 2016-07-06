package pl.srw.mfvp.view.delegate.presenter;

import pl.srw.mfvp.presenter.MvpPresenter;

/**
 * Delegate for managing single presenter according to component lifecycle
 */
public class SinglePresenterHandlingDelegate extends PresenterHandlingDelegate {

    private final MvpPresenter presenter;

    public SinglePresenterHandlingDelegate(PresenterOwner view, MvpPresenter presenter) {
        super(view);
        this.presenter = presenter;
    }

    @Override
    public void onStart() {
        presenter.bind(view);
    }

    @Override
    public void onStop() {
        presenter.unbind();
    }

    @Override
    public void onEnd() {
        presenter.onFinish();
    }
}
