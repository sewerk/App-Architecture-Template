package pl.srw.template.presenter;

import java.util.Collection;

import javax.inject.Inject;
import javax.inject.Provider;

import pl.srw.mfvp.di.scope.RetainActivityScope;
import pl.srw.mfvp.presenter.MvpPresenter;
import pl.srw.template.model.Todo;
import pl.srw.template.presenter.task.GetTask;
import pl.srw.template.presenter.task.PushTask;
import timber.log.Timber;

/**
 * List view presenter. Lives as long as activity.
 */
@RetainActivityScope
public class ListViewPresenter extends MvpPresenter<ListViewPresenter.ListView>
        implements GetTask.Caller {

    private Collection<Todo> entries;
    private GetTask getTask;
    private Provider<PushTask> pushTasks;

    @Inject
    public ListViewPresenter(GetTask getTask, Provider<PushTask> pushTasks) {
        this.getTask = getTask;
        this.pushTasks = pushTasks;
    }

    @Override
    protected void onFirstBind() {
        getTask.execute(this);
    }

    @Override
    protected void onNewViewRestoreState() {
        Timber.d("Displaying cached data");
        displayEntries();
    }

    public void checkboxClickedFor(Todo todo) {
        pushTasks.get().execute(new Todo(!todo.isDone(), todo.getText()));
    }

    @Override
    public void onDataRetrieved(Collection<Todo> todos) {
        entries = todos;
        displayEntries();
    }

    private void displayEntries() {
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
