package pl.srw.template.presenter.task;

import android.os.AsyncTask;

import javax.inject.Inject;

import pl.srw.template.model.Repository;
import pl.srw.template.model.Todo;
import timber.log.Timber;

/**
 * Async task for pushing data to repository
 */
public class PushTask extends AsyncTask<Todo, Void, Boolean> {

    private Repository repository;

    @Inject
    public PushTask(Repository repository) {
        this.repository = repository;
    }

    @Override
    protected Boolean doInBackground(Todo... params) {
        Timber.d("Pushing data...");
        return repository.push(params[0]);
    }

    @Override
    protected void onPostExecute(Boolean result) {
        Timber.d("New entry send result: %s", result);
    }
}
