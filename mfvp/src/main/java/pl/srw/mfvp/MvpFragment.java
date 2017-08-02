package pl.srw.mfvp;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.View;

import pl.srw.mfvp.di.MvpActivityScopedFragment;
import pl.srw.mfvp.di.MvpFragmentScopedFragment;

/**
 * Parent class for fragments-view in MVP model.
 * Features:
 *  - dependency injection is done every time fragment is created
 *  - releasing dependencies depends on associated scope component
 */
public abstract class MvpFragment extends DialogFragment {

    private boolean isFinishing;
    private PresenterHandlingDelegate presenterDelegate = new PresenterHandlingDelegate(this);

    @Override
    @CallSuper
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        injectDependencies();
    }

    @Override
    @CallSuper
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenterDelegate.onReady();
    }

    @Override
    @CallSuper
    public void onStart() {
        super.onStart();
        if (!presenterDelegate.isViewBind()) {
            presenterDelegate.onReady();
        }
    }

    @Override
    @CallSuper
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        presenterDelegate.onUnavailable();
    }

    @Override
    @CallSuper
    public void onDestroyView() {
        super.onDestroyView();
        if (presenterDelegate.isViewBind()) {
            presenterDelegate.onUnavailable();
        }
    }

    @Override
    @CallSuper
    public void onDestroy() {
        resetDependencies(isFinishing);
        super.onDestroy();
    }

    @Override
    @CallSuper
    public void onCancel(DialogInterface dialog) {
        endOfScope();
        super.onCancel(dialog);
    }

    @Override
    @CallSuper
    public void dismiss() {
        endOfScope();
        super.dismiss();
    }

    /**
     * Attach presenter to this view so that in can be bind/unbind when needed
     * Should be called in {@link #onCreate}
     * @param presenters at least one presenter
     */
    protected void attachPresenter(MvpPresenter... presenters) {
        presenterDelegate = new PresenterHandlingDelegate(this, presenters);
    }

    void endOfScope() {
        isFinishing = true;
    }

    MvpActivity getMvpActivity() {
        return (MvpActivity) super.getActivity();
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
            presenterDelegate.onFinish();
        }
    }
}
