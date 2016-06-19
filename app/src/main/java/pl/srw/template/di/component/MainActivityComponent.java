package pl.srw.template.di.component;

import dagger.Subcomponent;
import pl.srw.template.core.di.component.ActivityScopeComponent;
import pl.srw.template.core.di.component.FragmentInActivityScopeComponent;
import pl.srw.template.core.di.scope.RetainActivityScope;
import pl.srw.template.di.module.MainActivityModule;
import pl.srw.template.view.ListFragment;
import pl.srw.template.view.MainActivity;

@RetainActivityScope
@Subcomponent(modules = MainActivityModule.class)
public interface MainActivityComponent
        extends ActivityScopeComponent<MainActivity>,
        FragmentInActivityScopeComponent<ListFragment> {

    AddFragmentComponent getAddFragmentComponent();
}
