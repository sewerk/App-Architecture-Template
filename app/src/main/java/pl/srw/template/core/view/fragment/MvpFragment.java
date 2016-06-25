package pl.srw.template.core.view.fragment;

import android.app.Application;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import pl.srw.template.core.MvpApplication;
import pl.srw.template.core.di.DependencyComponentManager;
import pl.srw.template.core.view.activity.MvpActivity;
import pl.srw.template.core.view.delegate.LifeCycleListener;
import pl.srw.template.core.view.delegate.presenter.PresenterOwner;

/**
 * Parent class for fragments-view in MVP model.
 * Features:
 *  - dependency injection is done every time fragment is created
 *  - releasing dependencies depends on associated scope component
 *  - lifecycle events will be communicated to added listeners
 */
public abstract class MvpFragment extends Fragment {

    private List<LifeCycleListener> listeners;

    public MvpFragment() {
        listeners = new ArrayList<>(1);
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
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
    }

    @Override
    @CallSuper
    public void onStart() {
        super.onStart();
        for (LifeCycleListener listener : listeners) {
            listener.onStart();
        }
    }

    @Override
    @CallSuper
    public void onStop() {
        super.onStop();
        for (LifeCycleListener listener : listeners) {
            listener.onStop();
        }
    }

    @Override
    @CallSuper
    public void onDestroyView() {
        ButterKnife.unbind(this);
        super.onDestroyView();
    }

    /**
     * Callback when view if out of scope and all dependent objects need to be destroyed
     */
    @CallSuper
    public void endOfScope() {
        for (LifeCycleListener listener : listeners) {
            listener.onEnd();
        }
        resetDependencies();
    }

    public MvpActivity getBaseActivity() {
        return (MvpActivity) super.getActivity();
    }

    /**
     * Add listener to this fragment lifecycle
     * @param listener    lifecycle listener
     */
    public final void addListener(LifeCycleListener listener) {
        listeners.add(listener);
    }

    private void injectDependencies() {
        if (this instanceof MvpFragmentScopedFragment) {
            final MvpFragmentScopedFragment fragment = (MvpFragmentScopedFragment) this;
            getDependencyManager().getComponentFor(fragment).inject(fragment);
        } else if (this instanceof MvpActivityScopedFragment){
            final MvpActivityScopedFragment fragment = (MvpActivityScopedFragment) this;
            getDependencyManager().getComponentFor(fragment).inject(fragment);
        }
        throw new ClassCastException("MvpFragment must implement " +
                "one of interfaces: MvpFragmentScopedFragment or MvpActivityScopedFragment");
    }

    private void resetDependencies() {
        if (this instanceof MvpFragmentScopedFragment) {
            final MvpFragmentScopedFragment fragment = (MvpFragmentScopedFragment) this;
            getDependencyManager().releaseComponentFor(fragment);
        }
        // else dependencies will be reset by activity
    }

    private DependencyComponentManager getDependencyManager() {
        final Application application = getActivity().getApplication();
        if (application instanceof MvpApplication) {
            ((MvpApplication) application).getDependencies();
        }
        throw new ClassCastException("Application class must extend MvpApplication");
    }
}
