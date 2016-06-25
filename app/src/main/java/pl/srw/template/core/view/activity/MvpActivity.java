package pl.srw.template.core.view.activity;

import android.app.Application;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import pl.srw.template.R;
import pl.srw.template.core.MvpApplication;
import pl.srw.template.core.di.DependencyComponentManager;
import pl.srw.template.core.view.delegate.LifeCycleListener;
import pl.srw.template.core.view.delegate.presenter.PresenterOwner;
import pl.srw.template.core.view.fragment.MvpFragment;
import pl.srw.template.core.di.component.MvpActivityScopeComponent;
import pl.srw.template.core.view.fragment.MvpFragmentScopedFragment;
import timber.log.Timber;

/**
 * Parent class for Activity-view in MVP model.
 * Features:
 *  - dependency injection is done every time activity is created
 *  - releasing dependencies happens when activity is finishing
 *  - lifecycle events will be communicated to added listeners
 *  - provide common methods for fragment management
 * See also {@link pl.srw.template.core.di.scope.RetainActivityScope}
 */
public abstract class MvpActivity<C extends MvpActivityScopeComponent> extends AppCompatActivity {

    private List<LifeCycleListener> listeners;

    public MvpActivity() {
        this.listeners = new ArrayList<>(1);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentLayoutId());
        ButterKnife.bind(this);
        injectDependencies();
        if (this instanceof PresenterOwner) {
            PresenterOwner presenterActivity = (PresenterOwner) this;
            addListener(presenterActivity.createPresenterDelegate());
        }
    }

    @Override
    @CallSuper
    protected void onStart() {
        super.onStart();
        for (LifeCycleListener listener : listeners) {
            listener.onStart();
        }
    }

    @Override
    @CallSuper
    protected void onStop() {
        super.onStop();
        for (LifeCycleListener listener : listeners) {
            listener.onStop();
        }
    }

    @Override
    protected void onDestroy() {
        if (isFinishing()) {
            notifyStackedFragmentsActivityIsFinishing();
            resetDependencies();
            for (LifeCycleListener listener : listeners) {
                listener.onEnd();
            }
        }
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
            // if no more fragments to go back to then onDestroy will be called
        } else {
            notifyCurrentFragmentIsFinishing();
        }
        super.onBackPressed();
    }

    /**
     * Provides content layout resource id
     * @return layout id
     */
    @LayoutRes protected abstract int getContentLayoutId();

    /**
     * Add listener to this activity lifecycle
     * @param listener    lifecycle listener
     */
    public final void addListener(LifeCycleListener listener) {
        listeners.add(listener);
    }

    /**
     * Compose dependency graph to inject this dependencies
     */
    private void injectDependencies() {
        getDependencyManager().getComponentFor(this).inject(this);
    }

    /**
     * Release dependency graph
     */
    private void resetDependencies() {
        getDependencyManager().releaseComponentFor(this);
    }

    /**
     * Provides associated dependency component.
     * Component instance will be used to inject dependencies to this activity
     * and hold them according to scope.
     * @return component instance
     */
    public abstract C prepareComponent();

    /**
     * Replaces content fragment with adding to backstack
     * @param fragment    fragment to be shown
     * @param tag         fragment tag
     */
    protected void changeFragmentWithStack(Fragment fragment, String tag) {
        Timber.d("adding fragment " + fragment);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment, fragment, tag)
                .addToBackStack(null)
                .commit();
    }

    /**
     * Replaces content fragment without backstack
     * @param fragment    fragment to be shown
     * @param tag         fragment tag
     */
    protected void changeFragment(Fragment fragment, String tag) {
        Timber.d("changing fragment " + fragment);
        notifyStackedFragmentsAreFinishing();
        clearBackStack();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment, fragment, tag)
                .commit();
    }

    private void notifyStackedFragmentsActivityIsFinishing() {
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        if (fragments != null && !fragments.isEmpty()) {
            for (Fragment fragment : fragments) {
                if (fragment instanceof MvpFragment) {
                    // all fragment need to clean its dependencies now, since activity is finishing
                    ((MvpFragment) fragment).endOfScope();
                }
            }
        }
    }

    private void notifyStackedFragmentsAreFinishing() {
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        if (fragments != null && !fragments.isEmpty()) {
            for (Fragment fragment : fragments) {
                notifyFragmentIsFinishing(fragment);
            }
        }
    }

    private void notifyCurrentFragmentIsFinishing() {
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        if (fragments != null && !fragments.isEmpty()) {
            notifyFragmentIsFinishing(getLastVisibleFragment(fragments));
        }
    }

    private void notifyFragmentIsFinishing(Fragment fragment) {
        if (fragment instanceof MvpFragmentScopedFragment && fragment instanceof MvpFragment) {
            // fragment with own scope need to reset dependencies now, even when activity is not finishing
            ((MvpFragment) fragment).endOfScope();
        }
    }

    private void clearBackStack() {
        final FragmentManager manager = getSupportFragmentManager();
        for (int i = 0; i < manager.getBackStackEntryCount(); i++) {
            manager.popBackStack();
        }
    }

    @Nullable
    private Fragment getLastVisibleFragment(List<Fragment> fragments) {
        Fragment last;
        int idx = fragments.size() - 1;
        do {
            last = fragments.get(idx);
            idx--;
        } while (last == null && idx > 0);
        return last;
    }

    private DependencyComponentManager getDependencyManager() {
        final Application application = getApplication();
        if (application instanceof MvpApplication) {
            ((MvpApplication) application).getDependencies();
        }
        throw new ClassCastException("Application class must extend MvpApplication");
    }
}
