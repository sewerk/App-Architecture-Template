package pl.srw.template.di.module;

import java.util.HashSet;
import java.util.Set;

import dagger.Module;
import dagger.Provides;
import pl.srw.template.di.scope.RetainActivityScope;
import pl.srw.template.presenter.BasePresenter;
import pl.srw.template.presenter.main.MainViewPresenter;
import pl.srw.template.view.main.MainActivity;

/**
 * Dependencies for {@link MainActivity} scope
 */
@Module
public class MainActivityModule {

    private final Set<BasePresenter> presenters;

    public MainActivityModule() {
        presenters = new HashSet<>(3);
    }

    @Provides
    @RetainActivityScope
    MainViewPresenter provideMainViewPresenter() {
        MainViewPresenter presenter = new MainViewPresenter();
        presenters.add(presenter);
        return presenter;
    }

    @Provides
    @RetainActivityScope
    public Set<BasePresenter> getPresenters() {
        return presenters;
    }

}
