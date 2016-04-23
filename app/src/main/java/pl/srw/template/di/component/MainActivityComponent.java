package pl.srw.template.di.component;

import java.util.Set;

import dagger.Subcomponent;
import pl.srw.template.core.di.scope.RetainActivityScope;
import pl.srw.template.core.presenter.BasePresenter;
import pl.srw.template.di.module.MainActivityModule;
import pl.srw.template.view.ListFragment;
import pl.srw.template.view.MainActivity;

@RetainActivityScope
@Subcomponent(modules = MainActivityModule.class)
public interface MainActivityComponent {

    AddFragmentComponent getAddFragmentComponent();

    void inject(MainActivity activity);

    void inject(ListFragment listFragment);

    Set<BasePresenter> getPresenters();
}
