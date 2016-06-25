package pl.srw.template.core.di;

import java.util.HashMap;

import pl.srw.template.core.MvpApplication;
import pl.srw.template.core.di.component.ActivityScopeComponent;
import pl.srw.template.core.di.component.ApplicationScopeComponent;
import pl.srw.template.core.di.component.FragmentInActivityScopeComponent;
import pl.srw.template.core.di.component.FragmentScopeComponent;
import pl.srw.template.core.view.activity.MvpActivity;
import pl.srw.template.core.view.fragment.ActivityScopedFragment;
import pl.srw.template.core.view.fragment.OwnScopeFragment;

/**
 * Dependency components holder
 * @param <AC>    type of Dagger application component
 */
public final class DependencyComponentManager<AC extends ApplicationScopeComponent> {

    private final AC applicationComponent;
    private final HashMap<MvpActivity, ActivityScopeComponent> activityComponentsMap;
    private final HashMap<OwnScopeFragment, FragmentScopeComponent> fragmentComponentMap;

    public DependencyComponentManager(AC applicationComponent) {
        this.activityComponentsMap = new HashMap<>(1);
        this.fragmentComponentMap = new HashMap<>(0);
        this.applicationComponent = applicationComponent;
    }

    public static <C extends ApplicationScopeComponent> DependencyComponentManager<C> get(MvpApplication<C> application) {
        return application.getDependencies();
    }

    public AC getApplicationComponent() {
        return applicationComponent;
    }

    public <C extends ActivityScopeComponent> C getComponentFor(MvpActivity<C> activity) {
        if (!activityComponentsMap.containsKey(activity)) {
            activityComponentsMap.put(activity, activity.prepareComponent());
        }
        return (C) activityComponentsMap.get(activity);
    }

    public void releaseComponentFor(MvpActivity activity) {
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
        fragmentComponentMap.remove(fragment);
    }
}
