package pl.srw.template.core;

import android.app.Application;
import android.content.Context;
import android.support.annotation.CallSuper;

import pl.srw.template.core.di.DependencyComponentManager;

public class BaseApplication extends Application {

    private DependencyComponentManager dependencies;

    @CallSuper
    @Override
    public void onCreate() {
        super.onCreate();
        dependencies = new DependencyComponentManager(this);
    }

    /**
     * Gets dependency manager
     * @param context    context
     * @return dependency manager
     */
    public static DependencyComponentManager getDependencies(Context context) { //TODO
        BaseApplication application = (BaseApplication) context.getApplicationContext();
        return application.dependencies;
    }
}
