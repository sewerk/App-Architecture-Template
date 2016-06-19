package pl.srw.template.core.view;

import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import pl.srw.template.core.BaseApplication;
import pl.srw.template.core.di.ActivityScopedFragment;
import pl.srw.template.core.di.OwnScopeFragment;
import pl.srw.template.core.view.delegate.LifeCycleDelegating;
import pl.srw.template.core.view.delegate.presenter.EachViewNewPresenterOwner;
import pl.srw.template.core.view.delegate.presenter.PresenterOwner;

/**
 * Base class for fragments which needs additional functionality, i.e. binding with presenter
 */
public abstract class BaseFragment extends Fragment {

    private List<LifeCycleDelegating> delegates;

    @Override
    @CallSuper
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        delegates = new ArrayList<>();
        if (this instanceof PresenterOwner) {
            PresenterOwner presenterFragment = (PresenterOwner) this;
            presenterFragment.injectDependencies();
            delegates.add(presenterFragment.createPresenterDelegate());
        }
        // more delegates might come later
    }

    public final void injectDependencies() {
        if (this instanceof OwnScopeFragment) {
            final OwnScopeFragment fragment = (OwnScopeFragment) this;
            BaseApplication.getDependencies(getActivity()).getComponentFor(fragment).inject(fragment);
        } else if (this instanceof ActivityScopedFragment){
            final ActivityScopedFragment fragment = (ActivityScopedFragment) this;
            BaseApplication.getDependencies(getActivity()).getComponentFor(fragment).inject(fragment);
        }
    }

    public final void resetDependencies() { // TODO
        if (this instanceof OwnScopeFragment) {
            final OwnScopeFragment fragment = (OwnScopeFragment) this;
            BaseApplication.getDependencies(getActivity()).releaseComponentFor(fragment);
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
        for (LifeCycleDelegating delegate : delegates) {
            delegate.onStart();
        }
    }

    @Override
    @CallSuper
    public void onStop() {
        super.onStop();
        for (LifeCycleDelegating delegate : delegates) {
            delegate.onStop();
        }
    }

    @Override
    @CallSuper
    public void onDestroyView() {
        ButterKnife.unbind(this);
        super.onDestroyView();
    }

    /**
     * Call when Fragment is being destroyed without recreation
     */
    public void onFinishing() {
        for (LifeCycleDelegating delegate : delegates) {
            delegate.onFinish();
        }
        if (this instanceof EachViewNewPresenterOwner) {
            ((EachViewNewPresenterOwner) this).resetDependencies();
        }
    }
}
