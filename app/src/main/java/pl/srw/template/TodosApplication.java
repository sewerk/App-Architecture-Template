package pl.srw.template;

import timber.log.Timber;

/**
 * Application class
 */
public class TodosApplication extends pl.srw.template.core.BaseApplication {

    @Override
    public void onCreate() {
        super.onCreate();

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
    }
}
