package pl.srw.template.core.di;

import java.util.HashMap;
import java.util.Set;

import pl.srw.template.core.BaseApplication;
import pl.srw.template.core.di.component.ActivityScopeComponent;
import pl.srw.template.core.di.component.FragmentInActivityScopeComponent;
import pl.srw.template.core.di.component.FragmentScopeComponent;
import pl.srw.template.core.di.component.MvpApplicationComponent;
import pl.srw.template.core.presenter.BasePresenter;
import pl.srw.template.core.view.BaseActivity;

/**
 * Dependency components holder
 * @param <AC>    type of Dagger application component
 */
public final class DependencyComponentManager<AC extends MvpApplicationComponent> {

    private final AC applicationComponent;
    private final HashMap<BaseActivity, ActivityScopeComponent> activityComponentsMap;
    private final HashMap<OwnScopeFragment, FragmentScopeComponent> fragmentComponentMap;

    public DependencyComponentManager(AC applicationComponent) {
        this.activityComponentsMap = new HashMap<>(1);
        this.fragmentComponentMap = new HashMap<>(0);
        this.applicationComponent = applicationComponent;
    }

    public static <C extends MvpApplicationComponent> DependencyComponentManager<C> get(BaseApplication<C> application) {
        return application.getDependencies();
    }

    public AC getApplicationComponent() {
        return applicationComponent;
    }

    public <C extends ActivityScopeComponent> C getComponentFor(BaseActivity<C> activity) {
        if (!activityComponentsMap.containsKey(activity)) {
            activityComponentsMap.put(activity, activity.prepareComponent());
        }
        return (C) activityComponentsMap.get(activity);
    }

    public void releaseComponentFor(BaseActivity activity) {
        final ActivityScopeComponent component = activityComponentsMap.get(activity);
        final Set<BasePresenter> presenters = component.getPresenters();
        for (BasePresenter presenter : presenters) {
            presenter.onFinish();
        }
        activityComponentsMap.remove(activity);
    }

    public <C extends FragmentInActivityScopeComponent> C getComponentFor(ActivityScopedFragment fragment) {
        final ActivityScopeComponent activityComponent = getComponentFor(fragment.getBaseActivity());
        return (C) activityComponent;
    }

    public <C extends FragmentScopeComponent> C getComponentFor(OwnScopeFragment<C> fragment) {
        if (!fragmentComponentMap.containsKey(fragment)) {
            fragmentComponentMap.put(fragment, fragment.prepareComponent());
        }
        return (C) fragmentComponentMap.get(fragment);
    }

    public void releaseComponentFor(OwnScopeFragment fragment) {
        final BasePresenter presenter = fragmentComponentMap.get(fragment).getPresenter();
        presenter.onFinish();
        fragmentComponentMap.remove(fragment);
    }
}
