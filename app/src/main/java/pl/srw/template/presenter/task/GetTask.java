package pl.srw.template.presenter.task;

import android.os.AsyncTask;

import java.util.Collection;

import javax.inject.Inject;

import pl.srw.template.model.Repository;
import pl.srw.template.model.Todo;
import timber.log.Timber;

/**
 * Async task for retrieving data from repository
 */
public class GetTask extends AsyncTask<Void, Void, Collection<Todo>> {

    private Caller caller;
    private Repository repository;

    @Inject
    public GetTask(Repository repository) {
        this.repository = repository;
    }

    @Override
    protected Collection<Todo> doInBackground(Void... params) {
        Timber.d("Requesting data...");
        return repository.get();
    }

    @Override
    protected void onPostExecute(Collection<Todo> todos) {
        if (caller != null) {
            caller.onDataRetrieved(todos);
        }
    }

    public void execute(Caller caller) {
        this.caller = caller;
        this.execute();
    }

    /**
     * Callback with get results
     */
    public interface Caller {
        void onDataRetrieved(Collection<Todo> todos);
    }
}
