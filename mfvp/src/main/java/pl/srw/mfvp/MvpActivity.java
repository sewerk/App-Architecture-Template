package pl.srw.mfvp;

import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import java.util.List;

import pl.srw.mfvp.presenter.PresenterHandlingDelegate;
import pl.srw.mfvp.presenter.PresenterOwner;
import pl.srw.mfvp.di.component.MvpComponent;
import pl.srw.mfvp.view.delegate.ViewStateListener;
import pl.srw.mfvp.view.delegate.ViewStateNotifier;
import pl.srw.mfvp.view.fragment.MvpFragmentScopedFragment;
import timber.log.Timber;

/**
 * Parent class for Activity-view in MVP model.
 * Features:
 *  - dependency injection is done every time activity is created
 *  - releasing dependencies happens when activity is finishing
 *  - lifecycle events will be communicated to added listeners
 *  - provide methods for fragment management to manage scoping
 * See also {@link pl.srw.mfvp.di.scope.RetainActivityScope}
 */
public abstract class MvpActivity<C extends MvpComponent> extends AppCompatActivity {

    private ViewStateNotifier notifier = new ViewStateNotifier();
    private PresenterHandlingDelegate presenterDelegate;

    @Override
    @CallSuper
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentLayoutId());
        injectDependencies();
        if (this instanceof PresenterOwner) {
            PresenterOwner presenterActivity = (PresenterOwner) this;
            presenterDelegate = presenterActivity.createPresenterDelegate();
            addStateListener(presenterDelegate);
        }
        notifier.notifyViewReady();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (!presenterDelegate.isViewBind()) {
            notifier.notifyViewReady();
        }
    }

    @Override
    @CallSuper
    protected void onStart() {
        super.onStart();
        notifier.notifyViewVisible();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        notifier.notifyViewUnavailable();
    }

    @Override
    @CallSuper
    protected void onStop() {
        notifier.notifyViewHidden();
        super.onStop();
    }

    @Override
    @CallSuper
    protected void onDestroy() {
        if (isFinishing()) {
            notifyStackedFragmentsActivityIsFinishing();
        }
        notifier.notifyViewUnavailable();
        resetDependencies(isFinishing());
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
     * @param listener    view state listener
     */
    public final void addStateListener(ViewStateListener listener) {
        notifier.register(listener);
    }

    /**
     * Provides associated dependency component.
     * Component instance will be used to inject dependencies to this activity
     * and hold them according to scope.
     * @return component instance
     */
    protected abstract C prepareComponent();

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
        Timber.v("adding fragment %s", fragment);
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
        Timber.v("changing fragment %s", fragment);
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
        DependencyComponentManager.getInstance().getComponentFor(this).inject(this);
    }

    private void resetDependencies(boolean isFinishing) {
        if (DependencyComponentManager.getInstance().releaseComponentFor(this, isFinishing)) {
            notifier.notifyFinishing();
        }
    }
}
