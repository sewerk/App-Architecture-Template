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
    }

    @Override
    public void onRestart() {
        for (int i = 0; i < presenters.length; i++) {
            presenters[i].onRestart(view);
        }
    }

    @Override
    public void onVisible() {
        for (int i = 0; i < presenters.length; i++) {
            presenters[i].onViewVisible(view);
        }
    }

    @Override
    public void onHidden() {
        for (int i = 0; i < presenters.length; i++) {
            presenters[i].onViewHidden(view);
        }
    }

    @Override
    public void onUnavailable() {
        for (int i = 0; i < presenters.length; i++) {
            presenters[i].unbind(view);
        }
    }

    @Override
    public void onFinish() {
        for (int i = 0; i < presenters.length; i++) {
            presenters[i].onFinish();
        }
    }
}
