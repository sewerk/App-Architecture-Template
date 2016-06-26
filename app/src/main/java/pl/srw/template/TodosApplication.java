package pl.srw.template;

import android.content.Context;

import pl.srw.template.core.MvpApplication;
import pl.srw.template.di.component.ApplicationComponent;
import pl.srw.template.di.component.DaggerApplicationComponent;
import pl.srw.template.di.module.ApplicationModule;

/**
 * Application class
 */
public class TodosApplication extends MvpApplication<ApplicationComponent> {

    private ApplicationComponent applicationComponent;

    @Override
    protected ApplicationComponent prepareApplicationComponent() {
        applicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .build();
        return applicationComponent;
    }

    public static TodosApplication get(Context context) {
        return (TodosApplication) context.getApplicationContext();
    }

    public ApplicationComponent getApplicationComponent() {
        return applicationComponent;
    }
}
