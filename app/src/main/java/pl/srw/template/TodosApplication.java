package pl.srw.template;

import android.content.Context;

import pl.srw.template.core.MvpApplication;
import pl.srw.template.core.di.DependencyComponentManager;
import pl.srw.template.di.component.ApplicationComponent;
import pl.srw.template.di.component.DaggerApplicationComponent;
import pl.srw.template.di.module.ApplicationModule;
import timber.log.Timber;

/**
 * Application class
 */
public class TodosApplication extends MvpApplication<ApplicationComponent> {

    @Override
    public void onCreate() {
        super.onCreate();

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
    }

    @Override
    protected ApplicationComponent prepareApplicationComponent() {
        return DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .build();
    }

    public static DependencyComponentManager<ApplicationComponent> getDependencies(Context context) {
        return ((TodosApplication) context.getApplicationContext()).getDependencies();
    }
}
