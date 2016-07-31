package pl.srw.todos.presenter;

import javax.inject.Inject;

import pl.srw.mfvp.di.scope.RetainFragmentScope;
import pl.srw.mfvp.presenter.MvpPresenter;
import pl.srw.todos.model.Todo;
import pl.srw.todos.presenter.task.PushTask;

/**
 * Add view presenter. Lives as long as fragment.
 */
@RetainFragmentScope
public class AddViewPresenter extends MvpPresenter<AddViewPresenter.AddView> {

    private PushTask pushTask;

    @Inject
    public AddViewPresenter(PushTask pushTask) {
        this.pushTask = pushTask;
    }

    public void addClicked(boolean done, String text) {
        pushTask.execute(new Todo(done, text));
        present(new UIChange<AddView>() {
            @Override
            public void change(AddView view) {
                view.close();
            }
        });
    }

    /**
     * Add view actions
     */
    public interface AddView {

        void close();
    }
}
