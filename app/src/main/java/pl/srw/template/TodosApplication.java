package pl.srw.template;

import android.app.Application;
import android.content.Context;

import pl.srw.template.di.DependencyComponentManager;
import timber.log.Timber;

/**
 * Application class
 */
public class TodosApplication extends Application {

    private DependencyComponentManager dependencies;

    @Override
    public void onCreate() {
        super.onCreate();
        dependencies = new DependencyComponentManager(this);

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
    }

    /**
     * Gets dependency manager
     * @param context    context
     * @return dependency manager
     */
    public static DependencyComponentManager getDependencies(Context context) {
        TodosApplication application = (TodosApplication) context.getApplicationContext();
        return application.dependencies;
    }
}
