package pl.srw.template.core.di;

import java.util.HashMap;

import pl.srw.template.core.MvpApplication;
import pl.srw.template.core.di.component.MvpActivityScopeComponent;
import pl.srw.template.core.di.component.MvpApplicationScopeComponent;
import pl.srw.template.core.di.component.MvpFragmentInActivityScopeComponent;
import pl.srw.template.core.di.component.MvpFragmentScopeComponent;
import pl.srw.template.core.view.activity.MvpActivity;
import pl.srw.template.core.view.fragment.MvpActivityScopedFragment;
import pl.srw.template.core.view.fragment.MvpFragmentScopedFragment;

/**
 * Dependency components holder
 * @param <AC>    type of Dagger application component
 */
public final class DependencyComponentManager<AC extends MvpApplicationScopeComponent> {

    private final AC applicationComponent;
    private final HashMap<MvpActivity, MvpActivityScopeComponent> activityComponentsMap;
    private final HashMap<MvpFragmentScopedFragment, MvpFragmentScopeComponent> fragmentComponentMap;

    public DependencyComponentManager(AC applicationComponent) {
        this.activityComponentsMap = new HashMap<>(1);
        this.fragmentComponentMap = new HashMap<>(0);
        this.applicationComponent = applicationComponent;
    }

    public static <C extends MvpApplicationScopeComponent> DependencyComponentManager<C> get(MvpApplication<C> application) {
        return application.getDependencies();
    }

    public AC getApplicationComponent() {
        return applicationComponent;
    }

    public <C extends MvpActivityScopeComponent> C getComponentFor(MvpActivity<C> activity) {
        if (!activityComponentsMap.containsKey(activity)) {
            activityComponentsMap.put(activity, activity.prepareComponent());
        }
        return (C) activityComponentsMap.get(activity);
    }

    public void releaseComponentFor(MvpActivity activity) {
        activityComponentsMap.remove(activity);
    }

    public <C extends MvpFragmentInActivityScopeComponent> C getComponentFor(MvpActivityScopedFragment fragment) {
        final MvpActivityScopeComponent activityComponent = getComponentFor(fragment.getBaseActivity());
        return (C) activityComponent;
    }

    public <C extends MvpFragmentScopeComponent> C getComponentFor(MvpFragmentScopedFragment<C> fragment) {
        if (!fragmentComponentMap.containsKey(fragment)) {
            fragmentComponentMap.put(fragment, fragment.prepareComponent());
        }
        return (C) fragmentComponentMap.get(fragment);
    }

    public void releaseComponentFor(MvpFragmentScopedFragment fragment) {
        fragmentComponentMap.remove(fragment);
    }
}
