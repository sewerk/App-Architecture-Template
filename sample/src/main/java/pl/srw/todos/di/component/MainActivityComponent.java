package pl.srw.todos.di.component;

import dagger.Subcomponent;
import pl.srw.mfvp.di.component.MvpActivityScopeComponent;
import pl.srw.mfvp.di.component.MvpFragmentInActivityScopeComponent;
import pl.srw.mfvp.di.scope.RetainActivityScope;
import pl.srw.todos.di.module.MainActivityModule;
import pl.srw.todos.view.ListFragment;
import pl.srw.todos.view.MainActivity;

@RetainActivityScope
@Subcomponent(modules = MainActivityModule.class)
public interface MainActivityComponent
        extends MvpActivityScopeComponent<MainActivity>,
        MvpFragmentInActivityScopeComponent<ListFragment> {

    AddFragmentComponent getAddFragmentComponent();
}
