package pl.srw.template.core;

import android.app.Application;
import android.support.annotation.CallSuper;

import pl.srw.template.BuildConfig;
import pl.srw.template.core.di.DependencyComponentManager;
import pl.srw.template.core.di.component.MvpApplicationScopeComponent;
import timber.log.Timber;

/**
 * Parent application class
 * @param <C>    type of Dagger application component
 */
public abstract class MvpApplication<C extends MvpApplicationScopeComponent> extends Application {

    private DependencyComponentManager<C> dependencies;

    @CallSuper
    @Override
    public void onCreate() {
        super.onCreate();

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }

        dependencies = new DependencyComponentManager<>(prepareApplicationComponent());
    }

    /**
     * Prepares Dagger application component
     * @return application component instance
     * */
    protected abstract C prepareApplicationComponent();

    /**
     * Gets dependency manager
     * @return dependency manager
     */
    public DependencyComponentManager<C> getDependencies() {
        return dependencies;
    }
}
