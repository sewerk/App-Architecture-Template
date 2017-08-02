package pl.srw.todos.presenter;

import javax.inject.Inject;

import pl.srw.mfvp.di.scope.RetainActivityScope;
import pl.srw.mfvp.MvpPresenter;

/**
 * Main view presenter
 */
@RetainActivityScope
public class MainViewPresenter extends MvpPresenter<MainViewPresenter.MainView> {

    private boolean addViewVisible;

    @Inject
    public MainViewPresenter() {
    }

    @Override
    protected void onFirstBind() {
        present(new UIChange<MainView>() {
            @Override
            public void change(MainView view) {
                view.showListView();
            }
        });
    }

    @Override
    protected void onNewViewRestoreState() {
        if (addViewVisible) {
            present(new UIChange<MainView>() {
                @Override
                public void change(MainView view) {
                    view.hideAddButton();
                }
            });
        }
    }

    public void addClicked() {
        addViewVisible = true;
        present(new UIChange<MainView>() {
            @Override
            public void change(MainView view) {
                view.showAddView();
                view.hideAddButton();
            }
        });
    }

    public void backPressed() {
        if (addViewVisible) {
            addViewVisible = false;
            present(new UIChange<MainView>() {
                @Override
                public void change(MainView view) {
                    view.showAddButton();
                }
            });
        }
    }

    /**
     * Main view actions
     */
    public interface MainView {

        void showAddView();

        void showListView();

        void hideAddButton();

        void showAddButton();
    }
}
