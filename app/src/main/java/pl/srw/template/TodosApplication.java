package pl.srw.template;

import android.content.Context;

import pl.srw.mfvp.MvpApplication;
import pl.srw.template.di.component.ApplicationComponent;
import pl.srw.template.di.component.DaggerApplicationComponent;
import pl.srw.template.di.module.ApplicationModule;
import timber.log.Timber;

/**
 * Application class
 */
public class TodosApplication extends MvpApplication {

    private ApplicationComponent applicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }

        applicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .build();
    }

    public static TodosApplication get(Context context) {
        return (TodosApplication) context.getApplicationContext();
    }

    public ApplicationComponent getApplicationComponent() {
        return applicationComponent;
    }
}
