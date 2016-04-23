package pl.srw.template.presenter;

import javax.inject.Inject;

import pl.srw.template.core.di.scope.RetainFragmentScope;
import pl.srw.template.core.presenter.BasePresenter;
import pl.srw.template.model.Repository;
import pl.srw.template.model.Todo;
import pl.srw.template.presenter.task.PushTask;

/**
 * Add view presenter. Lives as long as fragment.
 */
@RetainFragmentScope
public class AddViewPresenter extends BasePresenter<AddViewPresenter.AddView> {

    private Repository repository;

    @Inject
    public AddViewPresenter(Repository repository) {
        this.repository = repository;
    }

    public void addClicked(boolean done, String text) {
        new PushTask(repository).execute(new Todo(done, text));
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
