package pl.srw.template.view.delegate.presenter;

import pl.srw.template.presenter.BasePresenter;

/**
 * Delegate for managing multiple presenters according to component lifecycle
 */
public class MultiplePresenterHandlingDelegate extends PresenterHandlingDelegate {

    private final BasePresenter[] presenters;

    public MultiplePresenterHandlingDelegate(PresenterOwner view, BasePresenter... presenter) {
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
}
