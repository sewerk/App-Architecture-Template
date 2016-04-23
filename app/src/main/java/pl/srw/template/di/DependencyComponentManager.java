package pl.srw.template.di;

import pl.srw.template.TodosApplication;
import pl.srw.template.di.component.AddFragmentComponent;
import pl.srw.template.di.component.ApplicationComponent;
import pl.srw.template.di.component.DaggerApplicationComponent;
import pl.srw.template.di.component.MainActivityComponent;
import pl.srw.template.di.module.ApplicationModule;
import pl.srw.template.core.presenter.BasePresenter;

/**
 * Dependency components holder
 */
public final class DependencyComponentManager {

    private ApplicationComponent applicationComponent;
    private MainActivityComponent mainActivityComponent;
    private AddFragmentComponent addFragmentComponent;

    public DependencyComponentManager(TodosApplication application) {
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

    public AddFragmentComponent getAddFragmentComponent() {
        if (addFragmentComponent == null) {
            addFragmentComponent = mainActivityComponent.getAddFragmentComponent();
        }
        return addFragmentComponent;
    }

    public void releaseAddFragmentComponent() {
        addFragmentComponent.getPresenter().onFinish();
        addFragmentComponent = null;
    }
}
