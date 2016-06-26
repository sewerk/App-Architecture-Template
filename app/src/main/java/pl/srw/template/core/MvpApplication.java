package pl.srw.template.core;

import android.app.Application;
import android.content.Context;
import android.support.annotation.CallSuper;

import pl.srw.template.BuildConfig;
import pl.srw.template.core.di.DependencyComponentManager;
import timber.log.Timber;

/**
 * Parent application class
 */
public abstract class MvpApplication extends Application {

    private DependencyComponentManager dependencies;

    @CallSuper
    @Override
    public void onCreate() {
        super.onCreate();

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }

        dependencies = new DependencyComponentManager();
    }

    /**
     * Gets dependency manager
     * @return dependency manager
     */
    public static DependencyComponentManager getDependencies(Context context) {
        final Context applicationContext = context.getApplicationContext();
        if (applicationContext instanceof MvpApplication) {
            return ((MvpApplication) applicationContext).dependencies;
        }
        throw new ClassCastException("Application class must extend MvpApplication");
    }
}
