package pl.srw.template.presenter;

import java.util.Collection;

import javax.inject.Inject;

import pl.srw.template.core.di.scope.RetainActivityScope;
import pl.srw.template.core.presenter.MvpPresenter;
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
    private PushTask pushTask;

    @Inject
    public ListViewPresenter(GetTask getTask, PushTask pushTask) {
        this.getTask = getTask;
        this.pushTask = pushTask;
    }

    @Override
    protected void onFirstBind() {
        getTask.execute(this);
    }

    @Override
    protected void onNewViewRestoreState() {
        displayEntries();
    }

    public void checkboxClickedFor(Todo todo) {
        pushTask.execute(new Todo(!todo.isDone(), todo.getText()));
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
