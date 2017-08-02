package pl.srw.todos.di.component;

import dagger.Subcomponent;
import pl.srw.mfvp.di.MvpComponent;
import pl.srw.mfvp.di.scope.RetainFragmentScope;
import pl.srw.todos.view.AddFragment;

@RetainFragmentScope
@Subcomponent
public interface AddFragmentComponent extends MvpComponent<AddFragment> {

}
