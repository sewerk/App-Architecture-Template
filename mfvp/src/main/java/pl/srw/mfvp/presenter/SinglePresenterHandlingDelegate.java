package pl.srw.mfvp.presenter;

/**
 * Delegates associated owner lifecycle to single presenter
 */
public class SinglePresenterHandlingDelegate extends PresenterHandlingDelegate {

    private final MvpPresenter<PresenterOwner> presenter;

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
        presenter.unbind(view);
    }

    @Override
    public void onEnd() {
        presenter.onFinish();
    }
}
