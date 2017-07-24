package pl.srw.mfvp;

import java.util.HashMap;
import java.util.Map;

import pl.srw.mfvp.di.component.MvpComponent;
import pl.srw.mfvp.view.fragment.MvpActivityScopedFragment;
import pl.srw.mfvp.view.fragment.MvpFragmentScopedFragment;
import timber.log.Timber;

/**
 * Dependency components holder
 */
final class DependencyComponentManager {

    private final static DependencyComponentManager INSTANCE = new DependencyComponentManager();

    static DependencyComponentManager getInstance() {
        return INSTANCE;
    }

    private Map<Class, MvpComponent> components = new HashMap<>(5);
    private Map<Class, Integer> refCounter = new HashMap<>(5);

    private DependencyComponentManager() {
    }

    /**
     * Forces component preparation if such not exist for specific activity class
     * or return same component instance if it was not released
     *
     * @param mvpActivity activity instance
     * @return dependency injection component for activity
     */
    MvpComponent<MvpActivity> getComponentFor(MvpActivity mvpActivity) {
        return getOrAddComponent(mvpActivity);
    }

    /**
     * Forces component preparation if such not exist for specific fragment scoped fragment class
     * or return same component instance if it was not released. For fragment in activity scope
     * the activity component instance is returned
     *
     * @param mvpFragment fragment instance
     * @return dependency injection component for fragment
     * @throws IllegalStateException when activity for this fragment was not requesting component before
     */
    MvpComponent<MvpFragment> getComponentFor(MvpFragment mvpFragment) {
        Class<? extends MvpActivity> activityClass = mvpFragment.getMvpActivity().getClass();
        if (!components.containsKey(activityClass)) {
            throw new IllegalStateException("Fragment activity dependency component is missing");
        }
        if (mvpFragment instanceof MvpFragmentScopedFragment) {
            final MvpFragmentScopedFragment fragment = (MvpFragmentScopedFragment) mvpFragment;
            return getOrAddComponent(fragment, components.get(activityClass));
        } else if (mvpFragment instanceof MvpActivityScopedFragment){
            return components.get(activityClass);
        } else {
            throw new ClassCastException("MvpFragment must implement " +
                    "one of interfaces: MvpFragmentScopedFragment or MvpActivityScopedFragment");
        }
    }

    /**
     * Removes reference to dependency component if all instances stopped using it and this instance is finishing.
     * Releasing component for activity scoped fragment is delegated to activity itself making the release.
     *
     * @param instance component associated instance
     * @param isFinishing specifies if component is finishing
     * @param <T> associated instance type
     * @return true if component was released
     * @throws IllegalStateException when reference to component is missing or release called too many times
     */
    <T> boolean releaseComponentFor(T instance, boolean isFinishing) {
        if (instance instanceof MvpActivityScopedFragment) {
            // dependencies will be reset by activity
            return isFinishing;
        }

        final Class aClass = instance.getClass();
        if (!components.containsKey(aClass)) {
            throw new IllegalStateException("Reference to component is missing");
        }

        int count = refCounter.get(aClass);
        if (isFinishing && count == 1) {
            Timber.v("releasing component for %s", aClass.getSimpleName());
            components.remove(aClass);
            return true;
        } else {
            if (count - 1 < 0) {
                throw new IllegalStateException("Releasing of the component called too many times");
            }
            refCounter.put(aClass, count - 1);
        }
        return false;
    }

    private MvpComponent getOrAddComponent(MvpActivity instance) {
        final Class aClass = instance.getClass();
        if (!components.containsKey(aClass)) {
            Timber.v("preparing component for %s", aClass.getSimpleName());
            components.put(aClass, instance.prepareComponent());
            refCounter.put(aClass, 1);
        } else {
            Integer count = refCounter.get(aClass);
            refCounter.put(aClass, count + 1);
        }
        return components.get(aClass);
    }

    private MvpComponent<MvpFragment> getOrAddComponent(MvpFragmentScopedFragment instance,
                                                        MvpComponent<MvpActivity> activityComponent) {
        final Class aClass = instance.getClass();
        if (!components.containsKey(aClass)) {
            Timber.v("preparing component for %s", aClass.getSimpleName());
            components.put(aClass, instance.prepareComponent(activityComponent));
            refCounter.put(aClass, 1);
        } else {
            Integer count = refCounter.get(aClass);
            refCounter.put(aClass, count + 1);
        }
        return components.get(aClass);
    }
}
