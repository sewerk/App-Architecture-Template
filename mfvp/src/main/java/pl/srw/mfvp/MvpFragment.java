package pl.srw.mfvp;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;

import pl.srw.mfvp.di.component.MvpActivityScopeComponent;
import pl.srw.mfvp.view.delegate.LifeCycleListener;
import pl.srw.mfvp.view.delegate.LifeCycleNotifier;
import pl.srw.mfvp.view.delegate.presenter.PresenterOwner;
import pl.srw.mfvp.view.fragment.MvpActivityScopedFragment;
import pl.srw.mfvp.view.fragment.MvpFragmentScopedFragment;

/**
 * Parent class for fragments-view in MVP model.
 * Features:
 *  - dependency injection is done every time fragment is created
 *  - releasing dependencies depends on associated scope component
 *  - lifecycle events will be communicated to added listeners
 */
public abstract class MvpFragment extends DialogFragment {

    private LifeCycleNotifier notifier;
    private boolean endOfScopeOnDestroy;

    public MvpFragment() {
        notifier = new LifeCycleNotifier();
    }

    @Override
    @CallSuper
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        injectDependencies();
        if (this instanceof PresenterOwner) {
            PresenterOwner presenterFragment = (PresenterOwner) this;
            addListener(presenterFragment.createPresenterDelegate());
        }
    }

    @Override
    @CallSuper
    public void onStart() {
        super.onStart();
        notifier.notifyOnStart();
    }

    @Override
    @CallSuper
    public void onStop() {
        super.onStop();
        notifier.notifyOnStop();
    }

    @Override
    public void onDestroy() {
        if (endOfScopeOnDestroy) {
            endOfScope();
        }
        super.onDestroy();
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        if (this instanceof MvpFragmentScopedFragment) {
            endOfScopeOnDestroy = true;
        }
        super.onCancel(dialog);
    }

    @Override
    public void dismiss() {
        if (this instanceof MvpFragmentScopedFragment) {
            endOfScopeOnDestroy = true;
        }
        super.dismiss();
    }

    /**
     * Callback when view if out of scope and all dependent objects need to be destroyed
     */
    @CallSuper
    void endOfScope() {
        notifier.notifyOnEnd();
        resetDependencies();
    }

    protected MvpActivity getBaseActivity() {
        return (MvpActivity) super.getActivity();
    }

    /**
     * Add listener to this fragment lifecycle
     * @param listener    lifecycle listener
     */
    public final void addListener(LifeCycleListener listener) {
        notifier.register(listener);
    }

    private void injectDependencies() {
        final MvpActivityScopeComponent activityComponent = DependencyComponentManager.getInstance().getComponentFor(getBaseActivity());
        if (this instanceof MvpFragmentScopedFragment) {
            final MvpFragmentScopedFragment fragment = (MvpFragmentScopedFragment) this;
            DependencyComponentManager.getInstance().getComponentFor(fragment, activityComponent).inject(fragment);
        } else if (this instanceof MvpActivityScopedFragment){
            final MvpActivityScopedFragment fragment = (MvpActivityScopedFragment) this;
            fragment.injectDependencies(activityComponent);
        } else {
            throw new ClassCastException("MvpFragment must implement " +
                    "one of interfaces: MvpFragmentScopedFragment or MvpActivityScopedFragment");
        }
    }

    private void resetDependencies() {
        if (this instanceof MvpFragmentScopedFragment) {
            final MvpFragmentScopedFragment fragment = (MvpFragmentScopedFragment) this;
            DependencyComponentManager.getInstance().releaseComponentFor(fragment);
        }
        // else dependencies will be reset by activity
    }
}
