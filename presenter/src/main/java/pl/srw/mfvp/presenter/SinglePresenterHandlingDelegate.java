package pl.srw.mfvp.presenter;

/**
 * Delegates associated owner state to single presenter
 */
public class SinglePresenterHandlingDelegate extends PresenterHandlingDelegate {

    private final MvpPresenter<PresenterOwner> presenter;

    public SinglePresenterHandlingDelegate(PresenterOwner view, MvpPresenter presenter) {
        super(view);
        this.presenter = presenter;
    }

    @Override
    public void onReady() {
        presenter.bind(view);
    }

    @Override
    public void onRestart() {
        presenter.onRestart(view);
    }

    @Override
    public void onVisible() {
        presenter.onViewVisible(view);
    }

    @Override
    public void onHidden() {
        presenter.onViewHidden(view);
    }

    @Override
    public void onUnavailable() {
        presenter.unbind(view);
    }

    @Override
    public void onFinish() {
        presenter.onFinish();
    }
}
