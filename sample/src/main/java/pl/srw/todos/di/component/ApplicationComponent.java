package pl.srw.todos.di.component;

import javax.inject.Singleton;

import dagger.Component;
import pl.srw.todos.di.module.ApplicationModule;

@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent{

    MainActivityComponent getMainActivityComponent();
}