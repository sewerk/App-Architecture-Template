package pl.srw.template.presenter.main;

import pl.srw.template.presenter.BasePresenter;

/**
 * Main view presenter
 */
public class MainViewPresenter extends BasePresenter<MainView> {

    @Override
    protected void onFirstBind() {
        present(new UIChange<MainView>() {
            @Override
            public void change(MainView view) {
                view.showHelloWorld();
            }
        });
    }

    @Override
    protected void onNewViewRestoreState() {
        present(new UIChange<MainView>() {
            @Override
            public void change(MainView view) {
                view.showHelloWorldAgain();
            }
        });
    }
}
