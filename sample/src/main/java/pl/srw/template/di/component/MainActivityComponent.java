package pl.srw.template.di.component;

import dagger.Subcomponent;
import pl.srw.mfvp.di.component.MvpActivityScopeComponent;
import pl.srw.mfvp.di.component.MvpFragmentInActivityScopeComponent;
import pl.srw.mfvp.di.scope.RetainActivityScope;
import pl.srw.template.di.module.MainActivityModule;
import pl.srw.template.view.ListFragment;
import pl.srw.template.view.MainActivity;

@RetainActivityScope
@Subcomponent(modules = MainActivityModule.class)
public interface MainActivityComponent
        extends MvpActivityScopeComponent<MainActivity>,
        MvpFragmentInActivityScopeComponent<ListFragment> {

    AddFragmentComponent getAddFragmentComponent();
}
