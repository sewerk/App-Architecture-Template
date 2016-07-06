package pl.srw.mfvp;

import android.app.Application;
import android.content.Context;
import android.support.annotation.CallSuper;

/**
 * Parent application class
 */
public abstract class MvpApplication extends Application {

    private DependencyComponentManager dependencies;

    @CallSuper
    @Override
    public void onCreate() {
        super.onCreate();
        dependencies = new DependencyComponentManager();
    }

    /**
     * Gets dependency manager
     * @return dependency manager
     */
    static DependencyComponentManager getDependencies(Context context) {
        final Context applicationContext = context.getApplicationContext();
        if (applicationContext instanceof MvpApplication) {
            return ((MvpApplication) applicationContext).dependencies;
        }
        throw new ClassCastException("Application class must extend MvpApplication");
    }
}
