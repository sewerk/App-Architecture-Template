package pl.srw.template.presenter.main;

import pl.srw.template.presenter.BasePresenter;

/**
 * Main view presenter
 */
public class MainViewPresenter extends BasePresenter<MainView> {

    @Override
    protected void onFirstBind() {
        present(MainView::showHelloWorld);
    }

    @Override
    protected void onNewViewRestoreState() {
        present(MainView::showHelloWorldAgain);
    }
}
