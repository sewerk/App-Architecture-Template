package pl.srw.template.view.delegate.presenter;

import pl.srw.template.presenter.BasePresenter;

/**
 * Delegate for managing single presenter according to component lifecycle
 */
public class SinglePresenterHandlingDelegate extends PresenterHandlingDelegate {

    private final BasePresenter presenter;

    public SinglePresenterHandlingDelegate(PresenterOwner view, BasePresenter presenter) {
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
}
