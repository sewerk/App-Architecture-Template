package pl.srw.mfvp;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.View;

import pl.srw.mfvp.presenter.PresenterOwner;
import pl.srw.mfvp.view.delegate.ViewStateListener;
import pl.srw.mfvp.view.delegate.ViewStateNotifier;
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
    private ViewStateNotifier notifier = new ViewStateNotifier();
    private boolean wasStopped;

    @Override
    @CallSuper
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        injectDependencies();
        if (this instanceof PresenterOwner) {
            PresenterOwner presenterFragment = (PresenterOwner) this;
            addStateListener(presenterFragment.createPresenterDelegate());
        }
    }

    @Override
    @CallSuper
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        notifier.notifyViewReady();
    }

    @Override
    @CallSuper
    public void onStart() {
        if (wasStopped)
            notifier.notifyViewRestarted(); // restart after stop might mean background-ing the app which is not necessary restart scenario
        super.onStart();
        notifier.notifyViewVisible();
    }

    @Override
    @CallSuper
    public void onStop() {
        notifier.notifyViewHidden();
        super.onStop();
        wasStopped = true;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        notifier.notifyViewUnavailable();
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
     * @param listener    view state listener
     */
    public final void addStateListener(ViewStateListener listener) {
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
            notifier.notifyFinishing();
        }
    }
}
