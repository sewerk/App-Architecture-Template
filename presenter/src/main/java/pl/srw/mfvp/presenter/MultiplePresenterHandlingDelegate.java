package pl.srw.mfvp.presenter;

/**
 * Delegates associated owner state to multiple presenters
 */
public class MultiplePresenterHandlingDelegate extends PresenterHandlingDelegate {

    private final MvpPresenter<PresenterOwner>[] presenters;

    public MultiplePresenterHandlingDelegate(PresenterOwner view, MvpPresenter... presenter) {
        super(view);
        this.presenters = presenter;
    }

    @Override
    public void onReady() {
        for (int i = 0; i < presenters.length; i++) {
            presenters[i].bind(view);
        }
        isViewBind = true;
    }

    @Override
    public void onUnavailable() {
        for (int i = 0; i < presenters.length; i++) {
            presenters[i].unbind(view);
        }
        isViewBind = false;
    }

    @Override
    public void onFinish() {
        for (int i = 0; i < presenters.length; i++) {
            presenters[i].onFinish();
        }
    }
}
