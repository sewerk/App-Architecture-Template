package pl.srw.mfvp;

import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import java.util.List;

import butterknife.ButterKnife;
import pl.srw.mfvp.di.component.MvpActivityScopeComponent;
import pl.srw.mfvp.view.delegate.LifeCycleListener;
import pl.srw.mfvp.view.delegate.LifeCycleNotifier;
import pl.srw.mfvp.view.delegate.presenter.PresenterOwner;
import pl.srw.mfvp.view.fragment.MvpFragmentScopedFragment;
import timber.log.Timber;

/**
 * Parent class for Activity-view in MVP model.
 * Features:
 *  - dependency injection is done every time activity is created
 *  - releasing dependencies happens when activity is finishing
 *  - lifecycle events will be communicated to added listeners
 *  - provide common methods for fragment management
 * See also {@link pl.srw.mfvp.di.scope.RetainActivityScope}
 */
public abstract class MvpActivity<C extends MvpActivityScopeComponent> extends AppCompatActivity {

    private LifeCycleNotifier notifier;

    public MvpActivity() {
        notifier = new LifeCycleNotifier();
    }

    @Override
    @CallSuper
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
        notifier.notifyOnStart();
    }

    @Override
    @CallSuper
    protected void onStop() {
        super.onStop();
        notifier.notifyOnStop();
    }

    @Override
    @CallSuper
    protected void onDestroy() {
        if (isFinishing()) {
            notifyStackedFragmentsActivityIsFinishing();
            resetDependencies();
            notifier.notifyOnEnd();
        }
        super.onDestroy();
    }

    @Override
    @CallSuper
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
            // if no more fragments to go back to then onDestroy will be called
        } else {
            notifyCurrentFragmentIsFinishing();
        }
        super.onBackPressed();
    }

    /**
     * Add listener to this activity lifecycle
     * @param listener    lifecycle listener
     */
    public final void addListener(LifeCycleListener listener) {
        notifier.register(listener);
    }

    /**
     * Provides associated dependency component.
     * Component instance will be used to inject dependencies to this activity
     * and hold them according to scope.
     * @return component instance
     */
    public abstract C prepareComponent();

    /**
     * Provides content layout resource id
     * @return layout id
     */
    @LayoutRes protected abstract int getContentLayoutId();

    /**
     * Replaces content fragment with adding to backstack
     * @param fragmentResId     resource id for fragment placeholder
     * @param fragment          fragment to be shown
     * @param tag               fragment tag
     */
    protected void changeFragmentWithStack(int fragmentResId, Fragment fragment, String tag) {
        Timber.d("adding fragment %s", fragment);
        getSupportFragmentManager().beginTransaction()
                .replace(fragmentResId, fragment, tag)
                .addToBackStack(null)
                .commit();
    }

    /**
     * Replaces content fragment without backstack
     * @param fragmentResId     resource id for fragment placeholder
     * @param fragment          fragment to be shown
     * @param tag               fragment tag
     */
    protected void changeFragment(int fragmentResId, Fragment fragment, String tag) {
        Timber.d("changing fragment %s", fragment);
        notifyStackedFragmentsAreFinishing();
        clearBackStack();
        getSupportFragmentManager().beginTransaction()
                .replace(fragmentResId, fragment, tag)
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

    private void injectDependencies() {
        MvpApplication.getDependencies(this).getComponentFor(this).inject(this);
    }

    private void resetDependencies() {
        MvpApplication.getDependencies(this).releaseComponentFor(this);
    }

}
