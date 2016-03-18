package pl.srw.template.di;

import pl.srw.template.TemplateApplication;
import pl.srw.template.di.component.ApplicationComponent;
import pl.srw.template.di.component.DaggerApplicationComponent;
import pl.srw.template.di.component.MainActivityComponent;
import pl.srw.template.di.module.ApplicationModule;
import pl.srw.template.presenter.BasePresenter;

/**
 * Dagger component dependency holder
 */
public final class DependencyComponentManager {

    private ApplicationComponent applicationComponent;
    private MainActivityComponent mainActivityComponent;

    public DependencyComponentManager(TemplateApplication application) {
        applicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(application))
                .build();
    }

    public ApplicationComponent getApplicationComponent() {
        return applicationComponent;
    }

    public MainActivityComponent getMainActivityComponent() {
        if (mainActivityComponent == null) {
            mainActivityComponent = applicationComponent.getMainActivityComponent();
        }
        return mainActivityComponent;
    }

    public void releaseMainActivityComponent() {
        for (BasePresenter presenter : mainActivityComponent.getPresenters()) {
            presenter.onFinish();
        }
        mainActivityComponent = null;
    }
}
