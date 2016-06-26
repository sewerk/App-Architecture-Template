package pl.srw.template.di.component;

import javax.inject.Singleton;

import dagger.Component;
import pl.srw.template.di.module.ApplicationModule;

@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent{

    MainActivityComponent getMainActivityComponent();
}