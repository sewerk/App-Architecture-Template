package pl.srw.mfvp;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;

import pl.srw.mfvp.view.fragment.MvpActivityScopedFragment;
import pl.srw.mfvp.view.fragment.MvpFragmentScopedFragment;

/**
 * Parent class for fragments-view in MVP model.
 * Features:
 *  - dependency injection is done every time fragment is created
 *  - releasing dependencies depends on associated scope component
 *  - associated presenter will be bind and unbind from/to this view
 */
public abstract class MvpFragment<P extends MvpPresenter> extends DialogFragment {

    private boolean isFinishing;

    @Override
    @CallSuper
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        injectDependencies();
    }

    @Override
    @CallSuper
    public void onStart() {
        super.onStart();
        getPresenter().bind(this);
    }

    @Override
    @CallSuper
    public void onStop() {
        getPresenter().unbind(this);
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

    /**
     * Provides associated presenter instance
     * @return presenter
     */
    protected abstract P getPresenter();

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
            getPresenter().onFinish();
        }
    }
}
