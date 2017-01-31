package pl.srw.mfvp;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;

import pl.srw.mfvp.presenter.PresenterOwner;
import pl.srw.mfvp.view.delegate.LifeCycleListener;
import pl.srw.mfvp.view.delegate.LifeCycleNotifier;
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

    private boolean isFinishing;
    private LifeCycleNotifier notifier = new LifeCycleNotifier();

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
        notifier.notifyOnStop();
        super.onStop();
    }

    @Override
    public void onDestroy() {
        resetDependencies(isFinishing);
        super.onDestroy();
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        endOfScope();
        super.onCancel(dialog);
    }

    @Override
    public void dismiss() {
        endOfScope();
        super.dismiss();
    }

    void endOfScope() {
        isFinishing = true;
    }

    MvpActivity getMvpActivity() {
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
        if (this instanceof MvpFragmentScopedFragment) {
            DependencyComponentManager.getInstance().getComponentFor(this).inject(this);
        } else if (this instanceof MvpActivityScopedFragment){
            final MvpActivityScopedFragment fragment = (MvpActivityScopedFragment) this;
            fragment.injectDependencies(DependencyComponentManager.getInstance().getComponentFor(this));
        } else {
            throw new ClassCastException("MvpFragment must implement " +
                    "one of interfaces: MvpFragmentScopedFragment or MvpActivityScopedFragment");
        }
    }

    private void resetDependencies(boolean isFinishing) {
        if (DependencyComponentManager.getInstance().releaseComponentFor(this, isFinishing)) {
            notifier.notifyOnEnd();
        }
    }
}
