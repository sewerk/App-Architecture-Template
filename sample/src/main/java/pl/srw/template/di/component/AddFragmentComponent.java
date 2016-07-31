package pl.srw.template.di.component;

import dagger.Subcomponent;
import pl.srw.mfvp.di.component.MvpFragmentScopeComponent;
import pl.srw.mfvp.di.scope.RetainFragmentScope;
import pl.srw.template.view.AddFragment;

@RetainFragmentScope
@Subcomponent
public interface AddFragmentComponent extends MvpFragmentScopeComponent<AddFragment> {

}
