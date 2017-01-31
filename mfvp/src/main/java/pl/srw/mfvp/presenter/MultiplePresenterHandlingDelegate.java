package pl.srw.mfvp.presenter;

/**
 * Delegates associated owner lifecycle to multiple presenters
 */
public class MultiplePresenterHandlingDelegate extends PresenterHandlingDelegate {

    private final MvpPresenter<PresenterOwner>[] presenters;

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
            presenters[i].unbind(view);
        }
    }

    @Override
    public void onEnd() {
        for (int i = 0; i < presenters.length; i++) {
            presenters[i].onFinish();
        }
    }
}
