package pl.srw.template.view;

import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import java.util.List;

import butterknife.ButterKnife;
import pl.srw.template.R;
import pl.srw.template.view.delegate.presenter.PresenterHandlingDelegate;
import timber.log.Timber;

/**
 * Base class for any Activity with support of scoped dependencies.
 * During creation it force to inject dependencies, on finishing it force to clear dependencies module.
 * It also provide common methods for fragment management
 * See also {@link pl.srw.template.di.scope.RetainActivityScope}
 */
public abstract class BaseActivity extends AppCompatActivity {

    private PresenterHandlingDelegate presenterDelegate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentLayoutId());
        ButterKnife.bind(this);        injectDependencies();
        presenterDelegate = createPresenterDelegate();
    }

    @Override
    @CallSuper
    protected void onStart() {
        super.onStart();
        presenterDelegate.onStart();
    }

    @Override
    @CallSuper
    protected void onStop() {
        super.onStop();
        presenterDelegate.onStop();
    }

    @Override
    protected void onDestroy() {
        if (isFinishing()) {
            notifyStackedFragmentsFinishing();
            resetDependencies();
        }
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
            // if no more fragments to go back to then onDestroy will be called
        } else {
            notifyLastFragmentFinishing();
        }
        super.onBackPressed();
    }

    /**
     * Provides content layout resource id
     * @return layout id
     */
    @LayoutRes protected abstract int getContentLayoutId();

    /**
     * Provides presenter delegate implementation
     * @return presenter delegate
     */
    protected abstract PresenterHandlingDelegate createPresenterDelegate();

    /**
     * Compose dependency graph to inject this dependencies
     */
    protected abstract void injectDependencies();

    /**
     * Release dependency graph
     */
    protected abstract void resetDependencies();

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
        notifyStackedFragmentsFinishing();
        clearBackStack();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment, fragment, tag)
                .commit();
    }

    private void notifyStackedFragmentsFinishing() {
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        if (fragments != null && !fragments.isEmpty()) {
            for (Fragment fragment : fragments) {
                notifyFinishing(fragment);
            }
        }
    }

    private void notifyLastFragmentFinishing() {
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        if (fragments != null && !fragments.isEmpty()) {
            notifyFinishing(getLastVisibleFragment(fragments));
        }
    }

    private void notifyFinishing(Fragment fragment) {
        if (fragment instanceof BaseFragment) {
            BaseFragment base = (BaseFragment) fragment;
            base.onFinishing();
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
}
