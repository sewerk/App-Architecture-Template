package pl.srw.template.di.module;

import java.util.HashSet;
import java.util.Set;

import dagger.Module;
import dagger.Provides;
import pl.srw.template.core.di.scope.RetainActivityScope;
import pl.srw.template.core.presenter.BasePresenter;
import pl.srw.template.presenter.ListViewPresenter;
import pl.srw.template.presenter.MainViewPresenter;
import pl.srw.template.presenter.task.GetTask;
import pl.srw.template.presenter.task.PushTask;
import pl.srw.template.view.MainActivity;

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
    ListViewPresenter provideListViewPresenter(GetTask getTask, PushTask pushTask) {
        final ListViewPresenter presenter = new ListViewPresenter(getTask, pushTask);
        presenters.add(presenter);
        return presenter;
    }

    @Provides
    @RetainActivityScope
    public Set<BasePresenter> getPresenters() {
        return presenters;
    }

}
