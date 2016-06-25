package pl.srw.template.di.component;

import dagger.Subcomponent;
import pl.srw.template.core.di.component.MvpFragmentScopeComponent;
import pl.srw.template.core.di.scope.RetainFragmentScope;
import pl.srw.template.view.AddFragment;

@RetainFragmentScope
@Subcomponent
public interface AddFragmentComponent extends MvpFragmentScopeComponent<AddFragment> {

}
