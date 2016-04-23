package pl.srw.template.presenter;

import java.util.Collection;

import pl.srw.template.core.di.scope.RetainActivityScope;
import pl.srw.template.core.presenter.BasePresenter;
import pl.srw.template.model.Repository;
import pl.srw.template.model.Todo;
import pl.srw.template.presenter.task.GetTask;
import pl.srw.template.presenter.task.PushTask;
import timber.log.Timber;

/**
 * List view presenter. Lives as long as activity.
 */
@RetainActivityScope
public class ListViewPresenter extends BasePresenter<ListViewPresenter.ListView>
        implements GetTask.Caller {

    private Repository repository;

    private Collection<Todo> entries;

    public ListViewPresenter(Repository repository) {
        this.repository = repository;
    }

    @Override
    protected void onFirstBind() {
        new GetTask(this, repository).execute();
    }

    @Override
    protected void onNewViewRestoreState() {
        displayEntries();
    }

    public void checkboxClickedFor(Todo todo) {
        new PushTask(repository).execute(new Todo(!todo.isDone(), todo.getText()));
    }

    @Override
    public void onDataRetrieved(Collection<Todo> todos) {
        entries = todos;
        displayEntries();
    }

    private void displayEntries() {
        Timber.d("Displaying cached data");
        present(new UIChange<ListView>() {
            @Override
            public void change(ListView view) {
                view.showEntries(entries);
            }
        });
    }

    /**
     * List view actions
     */
    public interface ListView {

        void showEntries(Collection<Todo> entries);
    }
}
