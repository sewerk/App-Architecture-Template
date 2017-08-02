package pl.srw.todos.di.component;

import dagger.Subcomponent;
import pl.srw.mfvp.di.MvpComponent;
import pl.srw.mfvp.di.scope.RetainActivityScope;
import pl.srw.todos.di.module.MainActivityModule;
import pl.srw.todos.view.ListFragment;
import pl.srw.todos.view.MainActivity;

@RetainActivityScope
@Subcomponent(modules = MainActivityModule.class)
public interface MainActivityComponent extends MvpComponent<MainActivity> {

    AddFragmentComponent getAddFragmentComponent();

    void inject(ListFragment fragment);
}
