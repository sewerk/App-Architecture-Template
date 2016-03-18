package pl.srw.template.di.component;

import java.util.Set;

import dagger.Subcomponent;
import pl.srw.template.di.module.MainActivityModule;
import pl.srw.template.di.scope.RetainActivityScope;
import pl.srw.template.presenter.BasePresenter;
import pl.srw.template.view.main.MainActivity;

@RetainActivityScope
@Subcomponent(modules = MainActivityModule.class)
public interface MainActivityComponent {

    void inject(MainActivity activity);

    Set<BasePresenter> getPresenters();
}
