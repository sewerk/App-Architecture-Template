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
        isViewBind = true;
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
        isViewBind = false;
    }

    @Override
    public void onFinish() {
        presenter.onFinish();
    }
}
