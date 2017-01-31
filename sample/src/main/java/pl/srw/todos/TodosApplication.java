package pl.srw.todos;

import android.app.Application;
import android.content.Context;

import pl.srw.todos.di.component.ApplicationComponent;
import pl.srw.todos.di.component.DaggerApplicationComponent;
import pl.srw.todos.di.module.ApplicationModule;
import timber.log.Timber;

/**
 * Application class
 */
public class TodosApplication extends Application {

    private ApplicationComponent applicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }

        applicationComponent = DaggerApplicationComponent.create();
    }

    public static TodosApplication get(Context context) {
        return (TodosApplication) context.getApplicationContext();
    }

    public ApplicationComponent getApplicationComponent() {
        return applicationComponent;
    }
}
